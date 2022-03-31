# MyTaxi Clone APP

Simple app that shows all the vehicle data in the bounds of Hamburg


1 - the app displays a list of Vehicles in a bottomSheet inside a recyclerView, recyclerview which has
    animation at the time of displaying data from the API response (data showing fleetType, and the exact address of the vehicle taken from the coordinates response, and a picture of the type of vehicle).

2 - When pressing one of the items in the recyclerview, the camera will zoom 15.0f in that specific Vehicle on the map.

3 - A googleMap will be displayed in a Fragment containing all the vehicles in the city of Hamburg.
    every marker will show an infoWindow with the exact location(address of the vehicle), once being touched on.

4- Button that shows a global view of all the vehicles in the city of Hamburg.

- For Achieving all this, the app implements a MVVM as pattern design, in order to modulate, and test the simple logic of getting all the vehicles.
- This MVVM contains in its constructor, a Repository, and a CoroutinesTestRule, for a better response, as well as testing purposes.
- Dagger-Hilt are the dependency injections choose to help with reduction of the amount of code.
- Material design components.
- Retrofit and OKHTTP are the clients chosen for working with the API response.

# Technologies.

Kotlin.

Retrofit.

Dagger-Hilt.

Minimum SDK level 21.

Coroutines.

Glide.

OkHttp3.

Material Components.

Jetpack:
- LifeData.
- Lifecycle.
- ViewModel
- Navigation Components.
- Data Binding.

MVVM.

Repository.

Unit testing MVVM
