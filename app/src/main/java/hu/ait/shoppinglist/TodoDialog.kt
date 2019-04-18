package hu.ait.shoppinglist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import hu.ait.shoppinglist.data.Item
import kotlinx.android.synthetic.main.activity_todo_dialog.*
import kotlinx.android.synthetic.main.activity_todo_dialog.view.*
import kotlinx.android.synthetic.main.item_row.*
import java.lang.RuntimeException
import java.util.*

class TodoDialog : DialogFragment(), AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    interface TodoHandler {
        fun itemCreated(item: Item)
        fun itemUpdated(item: Item)
    }
    var spinnerInt: Int = 0
    private lateinit var todoHandler: TodoHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is TodoHandler) {
            todoHandler = context
        } else {
            throw RuntimeException(
                "The activity does not implement the TodoHandlerInterface")
        }
    }

    private lateinit var etItemName: EditText
    private lateinit var etItemType: Spinner
    private lateinit var etItemPrice: EditText
    private lateinit var etItemDesc: EditText


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("New todo")

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.activity_todo_dialog, null
        )

        val fruitsAdapter =
            ArrayAdapter.createFromResource(context, R.array.cateName, android.R.layout.
                simple_spinner_item
        )

            fruitsAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )
            //itemType.adapter = fruitsAdapter

        //itemType.onItemSelectedListener = this

        //etTodoDate = rootView.findViewById(R.id.etTodoText)
        etItemName = rootView.itemName
        etItemType = rootView.itemType
        etItemPrice = rootView.itemPrice
        etItemDesc = rootView.itemDesc

        etItemType.adapter = fruitsAdapter


        builder.setView(rootView)

        val arguments = this.arguments

        // IF I AM IN EDIT MODE
        if (arguments != null && arguments.containsKey(
                ScrollingActivity.KEY_ITEM_TO_EDIT)) {

            val item = arguments.getSerializable(
                ScrollingActivity.KEY_ITEM_TO_EDIT
            ) as Item

            etItemName.setText(item.createName)
            etItemPrice.setText(item.createPrice)
            etItemDesc.setText(item.createDesc)

            if(item.createCate == "Food"){
                etItemType.setSelection(0)
            }
            else if(item.createCate == "Drink"){
                etItemType.setSelection(1)
            }
            else if(item.createCate == "Cosmetics"){
                etItemType.setSelection(2)
            }
            else if(item.createCate == "Electronics"){
                etItemType.setSelection(3)
            }

            builder.setTitle("Edit todo")
        }

        builder.setPositiveButton("ADD ITEM") {
                dialog, witch -> // empty
        }

        return builder.create()
    }


    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etItemName.text.isNotEmpty()) {
                val arguments = this.arguments
                // IF EDIT MODE
                if (arguments != null && arguments.containsKey(ScrollingActivity.KEY_ITEM_TO_EDIT)) {
                    handleItemEdit()
                } else {
                    handleItemCreate()
                }

                dialog.dismiss()
            }
            else{
                etItemName.error = "This field can not be empty"
            }
        }
    }

    private fun handleItemCreate() {

        todoHandler.itemCreated(

            Item(null, etItemName.text.toString(), etItemType.selectedItem.toString(), etItemPrice.text.toString(),

                etItemDesc.text.toString(), false))

        Item(

            null,

            Date(System.currentTimeMillis()).toString(),

            etItemType.selectedItem.toString(),

            etItemPrice.text.toString(),

            etItemDesc.text.toString(),

            false

        )
    }

    private fun handleItemEdit() {
        val todoToEdit = arguments?.getSerializable(
            ScrollingActivity.KEY_ITEM_TO_EDIT
        ) as Item
        todoToEdit.createName = etItemName.text.toString()
        todoToEdit.createCate = etItemType.selectedItem.toString()
        todoToEdit.createPrice = etItemPrice.text.toString()
        todoToEdit.createDesc = etItemDesc.text.toString()

        todoHandler.itemUpdated(todoToEdit)
    }

}
