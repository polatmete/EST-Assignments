package zest;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BusTrackingTest {
    private String busId ="bus_id";
    private String waypointname = "WaypointName";
    private Double latitude = 47.3769;
    private Double longitude = 8.5417;
    private Location location_keyWaypoint = new Location(latitude, longitude, true, waypointname);
    private Location location_notKeyWaypoint = new Location(latitude, longitude, false, waypointname);

    // Mock Services
    private GPSDeviceService gpsDeviceService = mock(GPSDeviceService.class);
    private MapService mapService = mock(MapService.class);
    private NotificationService notificationService = mock(NotificationService.class);


    private BusTracker busTracker = new BusTracker(gpsDeviceService, mapService, notificationService);


    @Test
    void isKeyWayPoint(){
        when(gpsDeviceService.getCurrentLocation(busId)).thenReturn(location_keyWaypoint);

        busTracker.updateBusLocation(busId);
        
        verify(mapService).updateMap(busId, location_keyWaypoint);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(notificationService).notifyPassengers(eq(busId), captor.capture());
        assertEquals("The bus has arrived at " + location_keyWaypoint.getWaypointName(), captor.getValue());
    }

    @Test
    void isNotKeyWayPoint(){
        when(gpsDeviceService.getCurrentLocation(busId)).thenReturn(location_notKeyWaypoint);

        busTracker.updateBusLocation(busId);

        verify(mapService).updateMap(busId, location_notKeyWaypoint);
        verifyNoInteractions(notificationService);
    }

    @Test
    void correctLocation(){
        when(gpsDeviceService.getCurrentLocation(busId)).thenReturn(location_keyWaypoint);
        busTracker.updateBusLocation(busId);

        ArgumentCaptor<Location> captorLocation = ArgumentCaptor.forClass(Location.class);
        verify(mapService).updateMap(eq(busId), captorLocation.capture());

        assertEquals(location_keyWaypoint, captorLocation.getValue());
    }


    @Test
    void GPSSignalLost(){
        location_keyWaypoint = null;
        when(gpsDeviceService.getCurrentLocation(busId)).thenReturn(location_keyWaypoint);

        busTracker.updateBusLocation(busId);

        verifyNoInteractions(mapService);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(notificationService).notifyPassengers(eq(busId), captor.capture());
        assertEquals("GPS signal lost. Please check back later.", captor.getValue());
    }
}
