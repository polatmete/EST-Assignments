package zest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BusTrackerTest {
    private final String busId ="bus_id";
    private final String waypointName = "WaypointName";
    private final Double latitude = 47.3769;
    private final Double longitude = 8.5417;
    private final Location location_keyWaypoint = new Location(latitude, longitude, true, waypointName);
    private final Location location_notKeyWaypoint = new Location(latitude, longitude, false, waypointName);

    // Mock Services
    private GPSDeviceService mockGPSDeviceService;
    private MapService mockMapService;
    private NotificationService mockNotificationService;
    private BusTracker busTracker;

    // Argument Captors
    private ArgumentCaptor<String> stringCaptor;
    private ArgumentCaptor<Location> locationCaptor;

    @BeforeEach
    void init(){
        mockGPSDeviceService = mock(GPSDeviceService.class);
        mockMapService = mock(MapService.class);
        mockNotificationService = mock(NotificationService.class);

        busTracker = new BusTracker(mockGPSDeviceService, mockMapService, mockNotificationService);

        stringCaptor = ArgumentCaptor.forClass(String.class);
        locationCaptor = ArgumentCaptor.forClass(Location.class);
    }

    @Test
    void isKeyWayPoint(){
        when(mockGPSDeviceService.getCurrentLocation(busId)).thenReturn(location_keyWaypoint);

        busTracker.updateBusLocation(busId);
        
        verify(mockMapService, times(1)).updateMap(busId, location_keyWaypoint);

        verify(mockNotificationService, times(1)).notifyPassengers(eq(busId), stringCaptor.capture());
        assertEquals("The bus has arrived at " + location_keyWaypoint.getWaypointName(), stringCaptor.getValue());
    }

    @Test
    void isNotKeyWayPoint(){
        when(mockGPSDeviceService.getCurrentLocation(busId)).thenReturn(location_notKeyWaypoint);

        busTracker.updateBusLocation(busId);

        verify(mockMapService, times(1)).updateMap(busId, location_notKeyWaypoint);
        verifyNoInteractions(mockNotificationService);
    }

    @Test
    void correctLocation(){
        when(mockGPSDeviceService.getCurrentLocation(busId)).thenReturn(location_keyWaypoint);
        busTracker.updateBusLocation(busId);


        verify(mockMapService, times(1)).updateMap(eq(busId), locationCaptor.capture());

        assertEquals(location_keyWaypoint, locationCaptor.getValue());
    }


    @Test
    void GPSSignalLost(){
        Location location = null;
        when(mockGPSDeviceService.getCurrentLocation(busId)).thenReturn(location);

        busTracker.updateBusLocation(busId);

        verifyNoInteractions(mockMapService);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockNotificationService, times(1)).notifyPassengers(eq(busId), captor.capture());
        assertEquals("GPS signal lost. Please check back later.", captor.getValue());
    }
}
