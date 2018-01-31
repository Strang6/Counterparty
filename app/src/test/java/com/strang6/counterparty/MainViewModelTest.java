package com.strang6.counterparty;

import com.google.android.gms.maps.model.LatLng;
import com.strang6.counterparty.ApiServices.DaDataService;
import com.strang6.counterparty.database.AddressCoordinatesDAO;
import com.strang6.counterparty.database.CounterpartyDatabase;
import com.strang6.counterparty.database.RecentCounterpartyDAO;
import com.strang6.counterparty.main.MainViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Strang6 on 13.01.2018.
 */
@Config(sdk = 21, manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class MainViewModelTest {
    private CounterpartyDatabase database;
    private RecentCounterpartyDAO recentCounterpartyDAO;
    private AddressCoordinatesDAO addressCoordinatesDAO;
    private DaDataService daDataService;
    private MainViewModel viewModel;

    @Before
    public void setUp() {
        database = Mockito.mock(CounterpartyDatabase.class);
        recentCounterpartyDAO = Mockito.mock(RecentCounterpartyDAO.class);
        Mockito.when(database.getRecentCounterpartyDAO()).thenReturn(recentCounterpartyDAO);
        addressCoordinatesDAO = Mockito.mock(AddressCoordinatesDAO.class);
        Mockito.when(database.getAddressCoordinatesDAO()).thenReturn(addressCoordinatesDAO);
        daDataService = Mockito.mock(DaDataService.class);

        viewModel = new MainViewModel(database, daDataService);
    }

    @Test
    public void testCreated() {
        assertNotNull(viewModel);
    }

    @Test
    public void onCounterpartyClickItemInBaseTest() {
        Counterparty counterparty = Mockito.mock(Counterparty.class);
        RecentCounterparty recentCounterparty = Mockito.mock(RecentCounterparty.class);
        Mockito.when(recentCounterpartyDAO.getItemByNameInnKpp(counterparty.getName(), counterparty.getInn(), counterparty.getKpp()))
                .thenReturn(recentCounterparty);

        viewModel.onCounterpartyClick(counterparty);
        Mockito.verify(recentCounterpartyDAO).updateItem(recentCounterparty);
    }

    @Test
    public void onCounterpartyClickNoItemInBaseWithLatLngTest() {
        LatLng latLng = new LatLng(3, 2);
        Counterparty counterparty = new Counterparty("name", "opf", "address", "inn", "kpp", "ogrn",
                5, Counterparty.BranchType.NULL, Counterparty.OrganizationType.INDIVIDUAL, latLng);
        int id = 4;
        RecentCounterparty recentCounterparty = new RecentCounterparty(id, counterparty, new Date(), false);
        Mockito.when(recentCounterpartyDAO.getItemByNameInnKpp(counterparty.getName(), counterparty.getInn(), counterparty.getKpp()))
                .thenReturn(null)
                .thenReturn(recentCounterparty);

        viewModel.onCounterpartyClick(counterparty);
        Mockito.verify(recentCounterpartyDAO).addRecentCounterparty(Mockito.any(RecentCounterparty.class));
        Mockito.verify(addressCoordinatesDAO).addCoordinates(Mockito.any(AddressCoordinates.class));
    }

    @Test
    public void onCounterpartyClickNoItemInBaseWithoutLatLngTest() {
        LatLng latLng = null;
        Counterparty counterparty = new Counterparty("name", "opf", "address", "inn", "kpp", "ogrn",
                5, Counterparty.BranchType.NULL, Counterparty.OrganizationType.INDIVIDUAL, latLng);
        int id = 4;
        RecentCounterparty recentCounterparty = new RecentCounterparty(id, counterparty, new Date(), false);
        Mockito.when(recentCounterpartyDAO.getItemByNameInnKpp(counterparty.getName(), counterparty.getInn(), counterparty.getKpp()))
                .thenReturn(null)
                .thenReturn(recentCounterparty);

        viewModel.onCounterpartyClick(counterparty);
        Mockito.verify(recentCounterpartyDAO).addRecentCounterparty(Mockito.any(RecentCounterparty.class));
        Mockito.verify(addressCoordinatesDAO, Mockito.never()).addCoordinates(Mockito.any(AddressCoordinates.class));
    }

    @Test
    public void onCounterpartyClickOnItemAddTest() {
        Counterparty counterparty = Mockito.mock(Counterparty.class);
        MainViewModel.CounterpartyListener listener = Mockito.mock(MainViewModel.CounterpartyListener.class);
        viewModel.setCounterpartyListener(listener);

        Mockito.when(recentCounterpartyDAO.getItemByNameInnKpp(counterparty.getName(), counterparty.getInn(), counterparty.getKpp()))
                .thenReturn(Mockito.mock(RecentCounterparty.class));

        viewModel.onCounterpartyClick(counterparty);
        Mockito.verify(listener).onItemAdd(Mockito.anyInt());
    }

    @After
    public void tearDown() {
        database = null;
        recentCounterpartyDAO = null;
        addressCoordinatesDAO = null;
        daDataService = null;
        viewModel = null;
    }
}
