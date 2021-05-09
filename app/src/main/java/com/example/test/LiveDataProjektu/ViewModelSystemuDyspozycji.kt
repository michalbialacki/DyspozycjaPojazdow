package com.example.test.LiveDataProjektu


import androidx.lifecycle.*
import com.example.test.Adapter.DaneRecycler
import com.example.test.Adapter.DriverDataClass
import com.example.test.Adapter.adminVehicleDataClass
import com.example.test.DataClasses.LocationLiveData
import java.text.SimpleDateFormat
import java.util.*

class ViewModelSystemuDyspozycji (private val savedStateHandle : SavedStateHandle): ViewModel() {
    val Latitude: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>(1.0)
    }

    val Longtitude: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>(1.0)
    }

    val PunktTrasy: MutableLiveData<String> by lazy {
        MutableLiveData<String>("1.0")
    }

    val PoczatkowyStanLicznika : MutableLiveData<Double> by lazy {
        MutableLiveData<Double>(170000.0)
    }

    val CelWyjazdu : MutableLiveData<String> by lazy {
        MutableLiveData<String>("Nie wprowadzono celu przejazdu")
    }

    val RodzajPrzewozu : MutableLiveData<String> by lazy {
        MutableLiveData<String>("Nie wprowadzono rodzaju przewozu")
    }

    val PunktStartowy : MutableLiveData<String> by lazy{
        MutableLiveData<String>("Wprowadź nazwę punktu startowego")
    }
    val PunktKoncowy : MutableLiveData<String> by lazy{
        MutableLiveData<String>("Wprowadź nazwę punktu końcowego")
    }

    val DrugiDysponent : MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }
    val RejestracjaPojazdu : MutableLiveData<String> by lazy {
        MutableLiveData<String>("WPROWADZWARTOSC")
    }
    val HasloUzytkownika : MutableLiveData<String> by lazy {
        MutableLiveData<String>("WartośćPoczątkowa")
    }
    val IDUzytkownika : MutableLiveData<String> by lazy {
        MutableLiveData<String>("11223344")
    }
    val przypisanePojazdydoKierowcy : MutableLiveData<String> by lazy {
        MutableLiveData<String>("WARTOSC")
    }
    val statusKierowcy : MutableLiveData<String> by lazy {
        MutableLiveData<String>("WARTOSC")
    }
    val statusPojazdu : MutableLiveData<String> by lazy {
        MutableLiveData<String>("STATUS POJAZDU")
    }
    val checkpointAddress : MutableLiveData<String> by lazy {
        MutableLiveData<String>("Miejsce początkowe")
    }
    val driverAdapterPosition : MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(-1)
    }
    val vehicleAdapterPosition : MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(-1)
    }

    var vehicleList = mutableListOf<String>()

    var Dzien  = SimpleDateFormat("ddMMyyyyHHMM").format(Date())

    var NazwaTrasy = mutableListOf<TrasaWPunktach>()


    var RozliczeniePojazdu = mutableListOf<DaneRecycler>()

    var adminDriversList = mutableListOf<DriverDataClass>()
    var adminVehicleList = mutableListOf<adminVehicleDataClass>()
    var driverListIDs = mutableListOf<String>()
    var driverNameList = mutableListOf<String>()
    var driverStatusList = mutableListOf<String>()
    var vehicleListIDs = mutableListOf<String>()
    var vehicleLastLocation = mutableListOf<String>()
    var vehicleStatus = mutableListOf<String>()
    var vehicleOdometer = mutableListOf<Double>()
    var vehicleType = mutableListOf<String>()


    fun zapiszWspolrzednePunktuTrasy(dana : Double?, zmienna : Double?) {

        Latitude.postValue(dana)
        Longtitude.postValue(zmienna)
    }

    fun dodajDoRozliczenia (wiersz : DaneRecycler) {
        RozliczeniePojazdu.add(wiersz)
    }
    fun zapiszPunktTrasy(wartoscPunktuTrasy : String?) {
            PunktTrasy.postValue(wartoscPunktuTrasy)
    }
    fun zapiszCelWyjazdu(celWyjazdu : String){
        CelWyjazdu.postValue(celWyjazdu)
    }
    fun zapiszRodzajPrzeowzu (rodzajPrzewozu : String){
        RodzajPrzewozu.postValue(rodzajPrzewozu)
    }
    fun PunktStartowyZapisz(punktStartowy:String) {
        PunktStartowy.postValue(punktStartowy)
    }
    fun PunktKoncowyZapisz(punktKoncowy:String) {
        PunktKoncowy.postValue(punktKoncowy)
    }
    fun NazwijTrase(Trasa : TrasaWPunktach){
        NazwaTrasy.add(Trasa)
    }
    fun DrugiDysponentDodaj(drugiDysponent : String){
        DrugiDysponent.postValue(drugiDysponent)
    }
    fun ZapiszDaneUzytkownika (hasloUzytkownika : String, idUzytkownika: String){
        HasloUzytkownika.postValue(hasloUzytkownika)
        IDUzytkownika.postValue(idUzytkownika)
    }
    fun dodajPojazdDoListyWyboru (nrRejestracjiPojazdu: String){

        przypisanePojazdydoKierowcy.postValue(nrRejestracjiPojazdu)

    }


    data class TrasaWPunktach (
        val punktStartowy: String,
        val punktKoncowy: String
            )
    //data class pozycjaWTablicyPrzypisanychPojazdow (val nrRejestracyjnyPojazdu : String)




}