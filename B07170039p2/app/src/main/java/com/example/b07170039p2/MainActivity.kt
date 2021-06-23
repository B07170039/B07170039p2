package com.example.b07170039p2

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class MainActivity : AppCompatActivity() {
    private var items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbrw: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //取得資料庫實體
        dbrw = MyDBHelper(this).writableDatabase

        val cv= ContentValues()
        cv.put("book","aaa")
        cv.put("price","aaa")
        dbrw.insert("mytable",null,cv)
        cv.put("book","bbb")
        cv.put("price","bbb")
        dbrw.insert("mytable",null,cv)
        cv.put("book","ccc")
        cv.put("price","ccc")
        dbrw.insert("mytable",null,cv)

        //宣告 Adapter 並連結 ListView
        adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, items)
        //findViewById<ListView>(R.id.listView).adapter = adapter
        //設定監聽器
        setListener()
    }
    override fun onDestroy() {
        dbrw.execSQL("INSERT INTO myTable(book, price) VALUES(aaa,aaa)")
        dbrw.close() //關閉資料庫
        super.onDestroy()
    }
    //設定監聽器
    private fun setListener() {
        val ed_book = findViewById<EditText>(R.id.ed_Username)
        val ed_price = findViewById<EditText>(R.id.ed_Password)



        findViewById<Button>(R.id.btn_query).setOnClickListener {
                //新增一筆書籍紀錄於 myTable 資料表


            //若無輸入書名則 SQL 語法為查詢全部書籍，反之查詢該書名資料
            if (ed_book.length() < 1 || ed_price.length() < 1)

                showToast("欄位請勿留空")
            else {

                val queryString = if (ed_book.length() < 1)
                    "SELECT * FROM myTable"
                else
                    "SELECT * FROM myTable WHERE book LIKE '${ed_book.text}' AND price LIKE '${ed_price.text}'"
                val c = dbrw.rawQuery(queryString, null)
                c.moveToFirst() //從第一筆開始輸出
                items.clear() //清空舊資料
                //showToast("共有${c.count}筆資料")
                if (c.count == 0) {
                    showToast("錯誤")
                } else {
                    showToast("正確")
                }

                adapter.notifyDataSetChanged() //更新列表資料
                c.close() //關閉 Cursor
            }
        }
    }
    //建立 showToast 方法顯示 Toast 訊息
    private fun showToast(text: String) =
        Toast.makeText(this,text, Toast.LENGTH_LONG).show()
    //清空輸入的書名與價格
    private fun cleanEditText() {
        findViewById<EditText>(R.id.ed_Username).setText("")
        findViewById<EditText>(R.id.ed_Password).setText("")
    }
}