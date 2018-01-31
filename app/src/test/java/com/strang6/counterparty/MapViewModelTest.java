package com.strang6.counterparty;

import com.google.android.gms.maps.model.LatLng;
import com.strang6.counterparty.ApiServices.GeocodingService;
import com.strang6.counterparty.database.AddressCoordinatesDAO;
import com.strang6.counterparty.database.CounterpartyDatabase;
import com.strang6.counterparty.database.RecentCounterpartyDAO;
import com.strang6.counterparty.map.MapViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * Created by Strang6 on 20.01.2018.
 */

@Config(sdk = 21, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class MapViewModelTest {
    private MapViewModel viewModel;
    private CounterpartyDatabase database;
    private AddressCoordinatesDAO addressCoordinatesDAO;
    private RecentCounterpartyDAO recentCounterpartyDAO;
    private GeocodingService service;

    @Before
    public void setUp() {
        database = Mockito.mock(CounterpartyDatabase.class);
        addressCoordinatesDAO = Mockito.mock(AddressCoordinatesDAO.class);
        Mockito.when(database.getAddressCoordinatesDAO()).thenReturn(addressCoordinatesDAO);
        recentCounterpartyDAO = Mockito.mock(RecentCounterpartyDAO.class);
        Mockito.when(database.getRecentCounterpartyDAO()).thenReturn(recentCounterpartyDAO);

        service = Mockito.mock(GeocodingService.class);

        viewModel = new MapViewModel(database, service);
    }

    @Test
    public void testCreated() {
        assertNotNull(viewModel);
    }

    @Test
    public void setIdCoordinatesInBaseTest() {
        LatLng latLng = new LatLng(4, 6);
        Mockito.when(addressCoordinatesDAO.getCoordinatesById(Mockito.anyString())).thenReturn(latLng);

        MapViewModel.LoadCoordinatesListener listener = Mockito.mock(MapViewModel.LoadCoordinatesListener.class);
        viewModel.setLoadCoordinatesListener(listener);

        int id = 2;
        viewModel.setId(id);

        Mockito.verify(addressCoordinatesDAO).getCoordinatesById(Mockito.matches(Integer.toString(id)));
        Mockito.verify(service, Mockito.never()).findLatLng(Mockito.anyString());
        Mockito.verify(listener).onLoadCoordinates(latLng);
        assertEquals(latLng, viewModel.getLatLng());
    }

    @Test
    public void setIdCoordinatesInServiceTest() {
        Mockito.when(addressCoordinatesDAO.getCoordinatesById(Mockito.anyString())).thenReturn(null);

        LatLng latLng = new LatLng(4, 6);
        Mockito.when(service.findLatLng(Mockito.anyString())).thenReturn(latLng);

        MapViewModel.LoadCoordinatesListener listener = Mockito.mock(MapViewModel.LoadCoordinatesListener.class);
        viewModel.setLoadCoordinatesListener(listener);

        int id = 2;

        Counterparty counterparty = new Counterparty("name", "opf", "address", "inn", "kpp", "ogrn",
                5, Counterparty.BranchType.NULL, Counterparty.OrganizationType.INDIVIDUAL, null);
        RecentCounterparty recentCounterparty = new RecentCounterparty(id, counterparty, new Date(), false);
        Mockito.when(recentCounterpartyDAO.getItemById(Mockito.anyString())).thenReturn(recentCounterparty);

        viewModel.setId(id);

        Mockito.verify(addressCoordinatesDAO).getCoordinatesById(Mockito.matches(Integer.toString(id)));
        Mockito.verify(service).findLatLng(counterparty.getAddress());
        Mockito.verify(addressCoordinatesDAO).addCoordinates(Mockito.any(AddressCoordinates.class));
        Mockito.verify(listener).onLoadCoordinates(latLng);
        assertEquals(latLng, viewModel.getLatLng());
    }

    @Test
    public void setIdNoCoordinates() {
        Mockito.when(addressCoordinatesDAO.getCoordinatesById(Mockito.anyString())).thenReturn(null);

        Mockito.when(service.findLatLng(Mockito.anyString())).thenReturn(null);

        MapViewModel.LoadCoordinatesListener listener = Mockito.mock(MapViewModel.LoadCoordinatesListener.class);
        viewModel.setLoadCoordinatesListener(listener);

        int id = 2;

        Counterparty counterparty = new Counterparty("name", "opf", "address", "inn", "kpp", "ogrn",
                5, Counterparty.BranchType.NULL, Counterparty.OrganizationType.INDIVIDUAL, null);
        RecentCounterparty recentCounterparty = new RecentCounterparty(id, counterparty, new Date(), false);
        Mockito.when(recentCounterpartyDAO.getItemById(Mockito.anyString())).thenReturn(recentCounterparty);

        viewModel.setId(id);

        Mockito.verify(addressCoordinatesDAO).getCoordinatesById(Mockito.matches(Integer.toString(id)));
        Mockito.verify(service).findLatLng(counterparty.getAddress());
        Mockito.verify(addressCoordinatesDAO, Mockito.never()).addCoordinates(Mockito.any(AddressCoordinates.class));
        Mockito.verify(listener).onLoadCoordinates(null);
        assertEquals(null, viewModel.getLatLng());
    }

    @Test
    public void setListenerCoordinatesIsLoadTest() {
        LatLng latLng = new LatLng(4, 6);
        Mockito.when(addressCoordinatesDAO.getCoordinatesById(Mockito.anyString())).thenReturn(latLng);

        int id = 2;
        viewModel.setId(id);

        MapViewModel.LoadCoordinatesListener listener = Mockito.mock(MapViewModel.LoadCoordinatesListener.class);
        viewModel.setLoadCoordinatesListener(listener);

        Mockito.verify(listener).onLoadCoordinates(latLng);
    }

    @Test
    public void setListenerCoordinatesNotLoadTest() {
        MapViewModel.LoadCoordinatesListener listener = Mockito.mock(MapViewModel.LoadCoordinatesListener.class);
        viewModel.setLoadCoordinatesListener(listener);

        Mockito.verify(listener, Mockito.never()).onLoadCoordinates(Mockito.any(LatLng.class));
    }

    @After
    public void tearDown() {
        database = null;
        service = null;
        viewModel = null;
    }
}
