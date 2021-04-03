package com.example.mygoogleanalystics

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_food.*
import java.util.*

class MainFoodActivity : AppCompatActivity() {
    var mFirebaseAnalytics: FirebaseAnalytics? = null
    var c = Calendar.getInstance()

    var year = c.get(Calendar.YEAR)
    var month = c.get(Calendar.MONTH)
    var day = c.get(Calendar.DAY_OF_MONTH)
    var hour = c.get(Calendar.HOUR_OF_DAY)
    var minute = c.get(Calendar.MINUTE)
    var second = c.get(Calendar.SECOND)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_food)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        track("Food Products screen")
        meals.setOnClickListener {
            btnEvent("meals@123","meals","button")

            var intent = Intent(this,MainMealsActivity::class.java)
            startActivity(intent)
        }

        dessert.setOnClickListener {
            btnEvent("dessert@123","dessert","button")

            var intent = Intent(this,MainDessertsActivity::class.java)
            startActivity(intent)
        }
        drinks.setOnClickListener {
            btnEvent("drinks@123","drinks","button")

            var intent = Intent(this,MainDrinksActivity::class.java)
            startActivity(intent)
        }
    }

    fun btnEvent(id : String , name : String , contentType : String){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "button")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
    fun track(screenName : String){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }


    override fun onPause() {
        val c = Calendar.getInstance()
        val hour2 = c.get(Calendar.HOUR_OF_DAY)
        val minute2 = c.get(Calendar.MINUTE)
        val second2 = c.get(Calendar.SECOND)

        var h = hour2 - hour
        var m =minute2 - minute
        var s = second2 - second

        Log.e("result","${h.toString()} ${m.toString()} ${s.toString()}")
        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        var uuid = UUID.randomUUID().toString();
        editor.putString("uuid", uuid)
        editor.commit()
        var db = FirebaseFirestore.getInstance()

        val user: MutableMap<String, Any> = HashMap()

        user["hour"] = h
        user["minute"] = m
        user["second"] = s
        user["id"] = uuid
        user["name"] = "Food Products screen"

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.e(

                    "DocumentSnapshot " , documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.e("Error adding document", e.toString()) }

        super.onPause()
    }

}
