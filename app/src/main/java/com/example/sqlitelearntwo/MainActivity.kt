package com.example.sqlitelearntwo

import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build.VERSION_CODES.S
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Visibility
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.forEachIndexed
import com.example.sqlitelearntwo.databinding.ActivityMainBinding
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    lateinit var db :SQLiteDatabase
    lateinit var rs :Cursor
    lateinit var adapter: SimpleCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var helper = MyHelper(applicationContext)
        db = helper.readableDatabase
        rs = db.rawQuery("SELECT * FROM ACTABLE ",null)

        adapter = SimpleCursorAdapter(applicationContext,android.R.layout.simple_expandable_list_item_2,rs,
            arrayOf("NAME","MEANING"),
            intArrayOf(android.R.id.text1,android.R.id.text2),0
            )
        binding.ListView.adapter = adapter

        //function call for context menu
        registerForContextMenu(binding.ListView)

        binding.ViewAllBtn.setOnClickListener {
            adapter.notifyDataSetChanged()

            binding.searchView.isIconified = false
            binding.searchView.queryHint = "Search Among ${rs.count} record"

            binding.ListView.visibility = View.VISIBLE
            binding.searchView.visibility = View.VISIBLE
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean { // here p0 is input text in SearchView
                rs = db.rawQuery("SELECT * FROM ACTABLE WHERE NAME LIKE '%${p0}%' OR MEANING LIKE '%${p0}%'", null)
                adapter.changeCursor(rs)
                return false
            }

        })


        binding.InsertBtn.setOnClickListener {
            //insert Record
            var cv  = ContentValues()
            cv.put("NAME",binding.Number.text.toString())
            cv.put("MEANING",binding.Regular.text.toString())
            db.insert("ACTABLE",null,cv)
            rs.requery()

            var ad = AlertDialog.Builder(this)
            ad.setTitle("Add record")
            ad.setMessage("Record Added sucessfully")
            ad.setPositiveButton("Ok",DialogInterface.OnClickListener{
                dialogInterface, i ->
                    binding.Number.setText("")
                    binding.Regular.setText("")
                    binding.Number.requestFocus()
            })

            ad.show()
        }
        binding.UpdateBtn.setOnClickListener {
            var cv = ContentValues()
            cv.put("NAME",binding.Number.text.toString())
            cv.put("MEANING",binding.Regular.text.toString())
            db.update("ACTABLE",cv,"_id = ?", arrayOf(rs.getString(0))) // here 0 is column Number
            rs.requery()

            var ad = AlertDialog.Builder(this)
            ad.setTitle("Update record")
            ad.setMessage("Record Updated sucessfully")
            ad.setPositiveButton("Ok",DialogInterface.OnClickListener{
                    dialogInterface, i ->
                if(rs.moveToFirst()){
                    binding.Number.setText(rs.getString(1))
                    binding.Regular.setText((rs.getString(2)))
                }
            })
            ad.show()
        }
        binding.DeleteBtn.setOnClickListener {
            db.delete("ACTABLE","_id = ?", arrayOf(rs.getString(0)))
            rs.requery()

            var ad = AlertDialog.Builder(this)
            ad.setTitle("Delete record")
            ad.setMessage("Record Deleted sucessfully")
            ad.setPositiveButton("Ok",DialogInterface.OnClickListener{
                    dialogInterface, i ->
                if(rs.moveToFirst()){
                    binding.Number.setText(rs.getString(1))
                    binding.Regular.setText((rs.getString(2)))
                }
            })
            ad.show()
        }

        binding.ClearBtn.setOnClickListener {
            binding.Number.setText("")
            binding.Regular.setText("")
            binding.Number.requestFocus()
        }

        //here traverse accross the each column
        binding.FirstBtn.setOnClickListener {
            if(rs.moveToFirst()){
                binding.Number.setText(rs.getString(1))//column 1
                binding.Regular.setText(rs.getString(2))//column 2
            }
            else{
                Toast.makeText(this, "No Data Available", Toast.LENGTH_SHORT).show()
            }
        }

        binding.NextBtn.setOnClickListener {
            if(rs.moveToNext()){
                binding.Number.setText(rs.getString(1))
                binding.Regular.setText((rs.getString(2)))
            }
            else if(rs.moveToFirst()){
                binding.Number.setText(rs.getString(1))
                binding.Regular.setText((rs.getString(2)))
            }
            else{
                Toast.makeText(this, "No Data Available", Toast.LENGTH_SHORT).show()
            }
        }

        binding.PrevBtn.setOnClickListener {
            if(rs.moveToPrevious()){
                binding.Number.setText(rs.getString(1))
                binding.Regular.setText((rs.getString(2)))
            }
            else if(rs.moveToLast()){
                binding.Number.setText(rs.getString(1))
                binding.Regular.setText((rs.getString(2)))
            }
            else{
                Toast.makeText(this, "No Data Available", Toast.LENGTH_SHORT).show()
            }
        }

        binding.LastBtn.setOnClickListener {
            if(rs.moveToLast()){
                binding.Number.setText(rs.getString(1))
                binding.Regular.setText((rs.getString(2)))
            }
            else{
                Toast.makeText(this, "No Data Available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.add(101,11,1,"DELETE")
        //.add(group_no,id_no,border_no,menu name)
        menu?.setHeaderTitle("Removing the Data")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if(item.itemId == 11){
            db.delete("ACTABLE","_id = ?", arrayOf(rs.getString(0)))
            rs.requery()
            adapter.notifyDataSetChanged()
            binding.searchView.queryHint = "Search Among ${rs.count} record"
        }

        return super.onContextItemSelected(item)

    }
}