package zest;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BusTrackingTest {
    private String busId ="bus_id";

    private GPSDeviceService gpsDeviceService = mock(GPSDeviceService.class);
    private MapService mapService = mock(MapService.class);
    private NotificationService notificationService = mock(NotificationService.class);
    private Location location = mock(Location.class);

    private BusTracker busTracker = new BusTracker(gpsDeviceService, mapService, notificationService);


    @Test
    void isKeyWayPoint(){
        when(location.isKeyWaypoint()).thenReturn(true);
        when(gpsDeviceService.getCurrentLocation(busId)).thenReturn(location);

        busTracker.updateBusLocation(busId);
        
        verify(mapService).updateMap(busId,location);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(notificationService).notifyPassengers(eq(busId), captor.capture());
        assertEquals("The bus has arrived at " + location.getWaypointName(), captor.getValue());
    }

    @Test
    void testMoreDetails(){
        String waypointname = "WaypointName";
        Double latitude = 47.3769;
        Double longitude = 8.5417;

        when(gpsDeviceService.getCurrentLocation(busId)).thenReturn(location);

        when(location.getLatitude()).thenReturn(latitude);
        when(location.getLongitude()).thenReturn(longitude);
        when(location.getWaypointName()).thenReturn(waypointname);

        busTracker.updateBusLocation(busId);

        ArgumentCaptor<Location> captorLocation = ArgumentCaptor.forClass(Location.class);
        verify(mapService).updateMap(eq(busId),captorLocation.capture());
        assertEquals(waypointname, captorLocation.getValue().getWaypointName());
        assertEquals(latitude, captorLocation.getValue().getLatitude());
        assertEquals(longitude, captorLocation.getValue().getLongitude());


    }

    @Test
    void isNotKeyWayPoint(){
        when(location.isKeyWaypoint()).thenReturn(false);
        when(gpsDeviceService.getCurrentLocation(busId)).thenReturn(location);

        busTracker.updateBusLocation(busId);

        verify(mapService).updateMap(busId,location);
        verifyNoInteractions(notificationService);
    }

    @Test
    void locationIsNull(){
        location = null;
        when(gpsDeviceService.getCurrentLocation(busId)).thenReturn(location);

        busTracker.updateBusLocation(busId);

        verifyNoInteractions(mapService);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(notificationService).notifyPassengers(eq(busId), captor.capture());
        assertEquals("GPS signal lost. Please check back later.", captor.getValue());
    }
}
