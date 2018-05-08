package com.fleenmobile.destinationcompass.feature.compass.presentation

import android.location.Location
import android.location.LocationManager
import com.fleenmobile.destinationcompass.BaseTest
import com.fleenmobile.destinationcompass.feature.compass.MainActivityContract
import com.fleenmobile.destinationcompass.feature.compass.view.Destination
import com.fleenmobile.destinationcompass.util.location.LocationDataProvider
import com.fleenmobile.destinationcompass.util.orientation.OrientationDataProvider
import com.fleenmobile.destinationcompass.util.rotation.RotationHelper
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyString
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

class MainActivityPresenterTest : BaseTest() {

    @Mock
    lateinit var view: MainActivityContract.View

    @Mock
    lateinit var orientationDataProvider: OrientationDataProvider

    @Mock
    lateinit var locationDataProvider: LocationDataProvider

    @Mock
    lateinit var compositeDisposable: CompositeDisposable

    @Mock
    lateinit var locationManager: LocationManager

    @Mock
    lateinit var rotationHelper: RotationHelper

    private lateinit var presenter: MainActivityPresenter

    override fun setup() {
        super.setup()

        presenter = MainActivityPresenter(
                view,
                orientationDataProvider,
                locationDataProvider,
                compositeDisposable,
                locationManager,
                rotationHelper
        )
    }

    override fun tearDown() {
        super.tearDown()
        verifyNoMoreInteractions(
                view,
                orientationDataProvider,
                locationDataProvider,
                compositeDisposable,
                locationManager,
                rotationHelper)
    }

    @Test
    fun `enforce permissions at startup`() {
        commonEnforcePrerequisitesPreparation()
        `when`(view.permissionsEnabled).thenReturn(false)
        `when`(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true)

        presenter.initialize()

        verify(view, times(1)).showPermissionsRequiredInfo()
        commonEnforcePrerequisitesVerification()
    }

    @Test
    fun `enforce GPS at startup`() {
        commonEnforcePrerequisitesPreparation()
        `when`(view.permissionsEnabled).thenReturn(true)
        `when`(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(false)

        presenter.initialize()

        verify(view, times(1)).showGPSEnabledRequiredInfo()
        commonEnforcePrerequisitesVerification()
    }

    private fun commonEnforcePrerequisitesPreparation() {
        val mockOrientation = 1.3f
        val mockLocation = Location("provider")

        `when`(orientationDataProvider.orientation()).thenReturn(Observable.just(mockOrientation))
        `when`(locationDataProvider.location()).thenReturn(Observable.just(mockLocation))
    }

    private fun commonEnforcePrerequisitesVerification() {
        verify(view, times(1)).permissionsEnabled
        verify(locationManager, times(1)).isProviderEnabled(LocationManager.GPS_PROVIDER)
        verify(orientationDataProvider, times(1)).orientation()
        verify(locationDataProvider, times(1)).location()
    }

    @Test
    fun `clear everything at clear method`() {
        presenter.clear()

        verify(compositeDisposable, times(1)).clear()
        verify(orientationDataProvider, times(1)).clear()
        verify(locationDataProvider, times(1)).clear()
    }

    @Test
    fun `show destination form on clicking change destination button`() {
        `when`(view.isArrowEnabled).thenReturn(false)

        presenter.changeDestinationClicked()

        verify(view, times(1)).showDestinationForm()
        verify(view, times(1)).isArrowEnabled
    }

    @Test
    fun `start updating compass on choosing destination`() {
        val mockRotation = 13.4f
        val mockDestination = Destination(13.4, 23.4)

        `when`(rotationHelper.calculateRotation(any(CompassData::class.java), any(Destination::class.java)))
                .thenReturn(mockRotation)
        commonStartUpdatingCompassPreparation()
        commonEnforcePrerequisitesPreparation()


        presenter.initialize()
        presenter.destinationChosen(mockDestination)

        commonDestinationClickedVerification()
        commonEnforcePrerequisitesVerification()
        verify(view, times(1)).rotateArrow(mockRotation)
        verify(rotationHelper, times(1))
                .calculateRotation(any(CompassData::class.java), any(Destination::class.java))
    }

    private fun commonStartUpdatingCompassPreparation() {
        `when`(view.permissionsEnabled).thenReturn(true)
        `when`(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true)
        `when`(view.isArrowEnabled).thenReturn(false)
    }

    @Test
    fun `show error in case orientation data fails`() {
        val mockDestination = Destination(13.4, 23.4)
        val mockLocation = Location("provider")

        `when`(orientationDataProvider.orientation()).thenReturn(Observable.error(IllegalStateException()))
        `when`(locationDataProvider.location()).thenReturn(Observable.just(mockLocation))
        commonStartUpdatingCompassPreparation()

        presenter.initialize()
        presenter.destinationChosen(mockDestination)

        commonDestinationClickedVerification()
        commonEnforcePrerequisitesVerification()
        verify(view, times(1)).showError(anyString())
    }

    @Test
    fun `show error in case location data fails`() {
        val mockDestination = Destination(13.4, 23.4)
        val mockOrientation = 1.3f

        `when`(orientationDataProvider.orientation()).thenReturn(Observable.just(mockOrientation))
        `when`(locationDataProvider.location()).thenReturn(Observable.error(IllegalStateException()))
        commonStartUpdatingCompassPreparation()

        presenter.initialize()
        presenter.destinationChosen(mockDestination)

        commonDestinationClickedVerification()
        commonEnforcePrerequisitesVerification()
        verify(view, times(1)).showError(anyString())
    }

    private fun commonDestinationClickedVerification() {
        verify(view, times(1)).showDestination("13,4", "23,4")
        verify(view, times(1)).isArrowEnabled
        verify(view, times(1)).enableArrow()
        verify(compositeDisposable, times(1)).add(any())
        verify(orientationDataProvider, times(1)).setup()
        verify(locationDataProvider, times(1)).setup()
    }

    @Test
    fun `show destination required popup on choosing incorrect destination`() {
        val mockDestination = Destination(null, null)

        `when`(view.permissionsEnabled).thenReturn(true)
        `when`(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true)
        `when`(view.isArrowEnabled).thenReturn(false)
        commonEnforcePrerequisitesPreparation()

        presenter.initialize()
        presenter.destinationChosen(mockDestination)

        commonEnforcePrerequisitesVerification()
        verify(view, times(1)).showDestination("-", "-")
        verify(view, times(1)).showDestinationRequiredInfo()
        verify(view, times(1)).isArrowEnabled
    }

    @Test
    fun `show permission required popup on rejecting permissions`() {
        presenter.permissionsNotGranted()

        verify(view, times(1)).showPermissionsRequiredInfo()
    }

    @Test
    fun `show GPS required popup on not turning GPS on`() {
        `when`(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(false)

        presenter.onGPSSettingsReturn()

        verify(view, times(1)).showGPSEnabledRequiredInfo()
        verify(locationManager, times(1)).isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @Test
    fun `show GPS required popup when GPS is not available`() {
        presenter.gpsNotEnabled()

        verify(view, times(1)).showGPSEnabledRequiredInfo()
    }

    @Test
    fun `don't start compass if it's already started`() {
        val mockDestination = Destination(12.3, 34.5)

        `when`(view.isArrowEnabled).thenReturn(true)

        presenter.destinationChosen(mockDestination)

        verify(view, times(1)).showDestination("12,3", "34,5")
        verify(view, never()).enableArrow()
        verify(orientationDataProvider, never()).setup()
        verify(locationDataProvider, never()).setup()
        verify(view, times(1)).isArrowEnabled
    }

    @Test
    fun `don't stop compass if it's already stopped`() {
        `when`(view.isArrowEnabled).thenReturn(false)

        presenter.changeDestinationClicked()

        verify(view, times(1)).isArrowEnabled
        verify(view, times(1)).showDestinationForm()
        verify(orientationDataProvider, never()).clear()
        verify(locationDataProvider, never()).clear()
        verify(view, never()).disableArrow()
    }
}