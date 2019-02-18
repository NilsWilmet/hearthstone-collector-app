package com.example.lpiem.hearthstonecollectorapp.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.lpiem.hearthstonecollectorapp.Adapter.CardsListAdapter
import com.example.lpiem.hearthstonecollectorapp.Fragments.CardsListFragment
import com.example.lpiem.hearthstonecollectorapp.Manager.HsUserManager
import com.example.lpiem.hearthstonecollectorapp.R
import com.example.lpiem.hearthstonecollectorapp.R.id.nav_cards
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.app_bar_navigation.*
import kotlinx.android.synthetic.main.nav_header_navigation.*
import kotlinx.android.synthetic.main.nav_header_navigation.view.*
import org.json.JSONObject

class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    internal lateinit var response: JSONObject
    internal lateinit var profile_pic_data:JSONObject
    internal lateinit var profile_pic_url:JSONObject

    private var hsUserManager = HsUserManager

    private var drawerLayout: androidx.drawerlayout.widget.DrawerLayout? = null

    private val decksFragment: androidx.fragment.app.Fragment? = null
    private val tradeFragment: androidx.fragment.app.Fragment? = null
    private val quizzFragment: androidx.fragment.app.Fragment? = null
    private val shopFragment: androidx.fragment.app.Fragment? = null

    private val FRAGMENT_CARDSLIST = 0
    private val FRAGMENT_DECKS = 1
    private val FRAGMENT_TRADE = 2
    private val FRAGMENT_QUIZZ = 3
    private val FRAGMENT_SHOP = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(toolbar)


        Log.d("InNav-User", hsUserManager.loggedUser.toString())
        Log.d("InNav-UserSocialInfos", hsUserManager.userSocialInfos.toString())

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val headerView = nav_view.getHeaderView(0)

        try {
            Log.d("[NavigationActivity]", hsUserManager.loggedUser.pseudo)
            headerView.nameUser.text = hsUserManager.loggedUser.pseudo

            if (hsUserManager.userSocialInfos.isNull("email")) {
                Log.d("NavigationActivity", "No social email, add default avatar")
                Glide.with(this).load("https://gazettereview.com/wp-content/uploads/2016/01/mind-control-tech-hearthstone-featured-tech.jpg").into(nav_view.getHeaderView(0).imgUser)
            } else {
                if (hsUserManager.userSocialInfos.get("email") != null) {
                    if (hsUserManager.userSocialInfos.get("type") == "f") { //On est connecté via Facebook
                        if (hsUserManager.userSocialInfos.get("picture") != null) { //Récupérer l'image de FB
                            var json = hsUserManager.userSocialInfos.get("picture") as JSONObject
                            var data = json.get("data") as JSONObject
                            var url = data.get("url") as String
                            Log.d("Facebook Image URL", url)
                            Glide.with(this).load(url).into(nav_view.getHeaderView(0).imgUser)
                        }
                    } else if (hsUserManager.userSocialInfos.get("type") == "g") {
                        if (hsUserManager.userSocialInfos.get("picture") != null) { //Récupérer l'image de FB
                            var url = hsUserManager.userSocialInfos.get("picture") as Uri
                            Log.d("Google Image URL", url.toString())
                            Glide.with(this).load(url.toString()).into(nav_view.getHeaderView(0).imgUser)
                            //Picasso.with(this).load(url).into(imgUser)
                        }
                    }
                }
            }

            nav_view.getHeaderView(0).moneyUser.text = hsUserManager.loggedUser.coins.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Fragment par défaut : liste des cartes
        val fragment = CardsListFragment.newInstance()
        replaceFragment(fragment)
        nav_view.getMenu().getItem(0).setChecked(true)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item
        // clicks here.
        when (item.itemId) {
            R.id.nav_cards -> {
                Log.d("[NavigationActivity]", "itemSelected !")
                val fragment = CardsListFragment.newInstance()
                replaceFragment(fragment)
            }
            R.id.nav_decks -> {
                //showFragment(new DecksFragment());
            }
            R.id.nav_trade -> {
                // showFragment(new TradeFragment());
            }
            R.id.nav_quizz -> {
                //showFragment(new QuizzFragment());
            }
            R.id.nav_shop -> {
                //showFragment(new ShopFragment());
            }
            R.id.nav_deconnexion -> {
                val intent = Intent(this@NavigationActivity, ConnexionActivity::class.java)
                intent.putExtra("deconnexion", true)
                startActivity(intent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        Log.d("[NavigationActivity]", "passe dans replaceFragment !")
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.content_navigation, fragment)
        fragmentTransaction.commit()
    }


}
