package com.example.lpiem.hearthstonecollectorapp.Interface

import com.example.lpiem.hearthstonecollectorapp.Models.Card
import com.example.lpiem.hearthstonecollectorapp.Models.Deck
import com.example.lpiem.hearthstonecollectorapp.Models.Friendship
import com.example.lpiem.hearthstonecollectorapp.Models.User
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface APIInterface {
    // NOTE SUR LES CALLS
    // Quand on fait un GET, l'API Symfony va parfois renvoyer un Array, même s'il n'y a qu'un seul résultat. On passe donc dans ce cas là des List d'objets au lieu des objets eux-mêmes.

    // CARDS //
    @GET("card/select/{id}")
    abstract fun getCard(@Path("id") id: Int): Call<Card>

    @GET("card/select/{set}")
    abstract fun getCardsBySet(@Path("set") set: String): Call<List<Card>>

    @GET("card/select/{class}")
    abstract fun getCardsByClass(@Path("class") classCard: String): Call<List<Card>>

    @GET("card/select/{race}")
    abstract fun getCardsByRace(@Path("race") race: String): Call<List<Card>>

    @GET("card/select/{faction}")
    abstract fun getCardsByFaction(@Path("faction") faction: String): Call<List<Card>>

    @GET("card/select-by-user/{userId}")
    abstract fun getCardsByUser(@Path("userId") userId: Int) : Call<List<Card>>

    @POST("card/new")
    abstract fun createCard(@Body card: Card): Call<Card>


    // USERS //
    @GET("/user/select/{id}")
    abstract fun getUser(@Path("id") id: Int): Call<User>

    @GET("/user/select-by-mail/{mail}")
    abstract fun getUserByMail(@Path("mail") mail: String): Call<User>

    @POST("/user/new")
    @Headers("Content-Type: application/json;charset=UTF-8")
    abstract fun createUser(@Body user: User): Call<User>

    @POST("/user/update")
    abstract fun updateUser(@Body user: User): Call<User>



    // DECKS //
    @GET("deck/select/{id}")
    abstract fun getDeck(@Path("id") id: Int): Call<Deck>

    @GET("/deck/select-by-user/{userId}")
    abstract fun getDecksByUser(@Path("userId") userId: Int): Call<MutableList<Deck>>

    @POST("/deck/new")
    @Headers("Content-Type: application/json;charset=UTF-8")
    abstract fun createDeck(@Body deck: Deck): Call<Deck>

    @POST("/deck/delete/{id}")
    abstract fun deleteDeck(@Path("id") id: Int): Call<Deck>

//    @POST("/user/set-deck")
//    abstract fun setDeckToUser(@Body user: User): Call<User>


    // USERS SYNC //
    @POST("/user/sync1")
    abstract fun syncUserStep1(@Body json: JsonObject): Call<JsonObject>

    @POST("/user/sync2/{arg}") //Arg is the value between Google and Facebook connection
    abstract fun syncUserStep2(@Path("arg") arg: String, @Body json: JsonObject): Call<JsonObject>


    // USERS LOGIN //
    @POST("/user/login")
    abstract fun checkLogin(@Body json: JsonObject): Call<User>

    // FRIENDSHIPS //
    @GET("/friendship/selectByUser/{userId}")
    abstract fun getFriendshipsByUser(@Path("userId") userId: Int): Call<List<Friendship>>

    @GET("/friendship/selectByUserPending/{userId}")
    abstract fun getPendingFriendshipsByUser(@Path("userId") userId: Int): Call<List<Friendship>>

    @GET("/friendship/delete/{friendshipId}")
    abstract fun deleteFriendship(@Path("friendshipId") friendshipId: Int): Call<JsonObject>



}