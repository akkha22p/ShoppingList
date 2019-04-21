package hu.ait.shoppinglist

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import hu.ait.shoppinglist.adapter.itemAdapter
import hu.ait.shoppinglist.data.AppDatabase
import hu.ait.shoppinglist.data.Item
import hu.ait.shoppinglist.touch.itemReyclerTouchCallback

import kotlinx.android.synthetic.main.activity_scrolling.*
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import java.util.*

class ScrollingActivity : AppCompatActivity(), TodoDialog.TodoHandler {


    companion object {
        val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
    }

    lateinit var todoAdapter: itemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_scrolling)

        setSupportActionBar(toolbar)

        if (!wasOpenedEarlier()) {

            MaterialTapTargetPrompt.Builder(this)

                .setTarget(R.id.createNewItem)

                .setPrimaryText("New TODO")

                .setSecondaryText("Click here to create new todo items")

                .show()

        }

        saveFirstOpenInfo()

        initRecyclerViewFromDB()

    }

    fun saveFirstOpenInfo() {
        var sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        var editor = sharedPref.edit()
        editor.putBoolean("KEY_WAS_OPEN", true)
        editor.apply()
    }

    fun wasOpenedEarlier(): Boolean {
        var sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        return sharedPref.getBoolean("KEY_WAS_OPEN", false)
    }

    private fun initRecyclerViewFromDB() {
        Thread {
            var todoList = AppDatabase.getInstance(this@ScrollingActivity).itemDao().getAllItem()

            // Update UI
            runOnUiThread {

                todoAdapter = itemAdapter(this, todoList)

                recyclerTodo.layoutManager = LinearLayoutManager(this)

                recyclerTodo.adapter = todoAdapter

                val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
                recyclerTodo.addItemDecoration(itemDecoration)

                val callback = itemReyclerTouchCallback(todoAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerTodo)
            }

        }.start()
    }

    private fun showAddTodoDialog() {
        TodoDialog().show(supportFragmentManager, "TAG_TODO_DIALOG")
    }


    var editIndex: Int = -1

    public fun showEditTodoDialog(todoToEdit: Item, idx: Int) {
        editIndex = idx
        val editItemDialog = TodoDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_TO_EDIT, todoToEdit)
        editItemDialog.arguments = bundle

        editItemDialog.show(
            supportFragmentManager,
            "EDITITEMDIALOG"
        )
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item?.itemId == R.id.createNewItem) {
            showAddTodoDialog()
        }

        else if (item?.itemId == R.id.deleteList) {
            Thread {
                AppDatabase.getInstance(this@ScrollingActivity).itemDao().deleteAll()
                runOnUiThread {
                    todoAdapter.removeAll()
                }
            }.start()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun itemCreated(item: Item) {
        Thread {
            var newId = AppDatabase.getInstance(this).itemDao().insertItem(item)

            item.itemId = newId

            runOnUiThread {
                todoAdapter.addItem(item)
            }
        }.start()
    }

    override fun itemUpdated(item: Item) {
        Thread {
            AppDatabase.getInstance(this).itemDao().updateItem(item)

            runOnUiThread {
                todoAdapter.updateItem(item, editIndex)
            }
        }.start()
    }

    public fun indexPosition(item: Item): Int{
        return editIndex
    }
}

