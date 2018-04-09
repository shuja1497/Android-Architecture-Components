package com.shuja1497.rooming

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    val appDatabase: AppDatabase? by lazy {
        AppDatabase.getInstance(this)
    }

    private lateinit var userDao : UserDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        userDao = appDatabase!!.userDao()

    }
    fun onClick(view: View?){
        when(view?.id) {

            R.id.button_insert -> {
                userDao.insertUser(User(editText_user_name.text.toString(), editText_id.text.toString().toInt()))
            }

            R.id.button_delete -> {
                val user = userDao.getUser(editText_id_delete.text.toString().toInt())
                userDao.deleteUser(user)
            }

            R.id.button_delete_all -> {
                userDao.deleteAllUser()
            }

            R.id.button_show_all -> {
                userDao.getAllUsers()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
