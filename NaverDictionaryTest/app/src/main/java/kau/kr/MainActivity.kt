package kau.kr

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import okhttp3.*
import java.io.IOException
import java.net.URL
import java.net.URLEncoder
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    val clientId = "sUoNOVQRX2ytJDihmSkH"
    val clientSecret = "MoraHUpncQ"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_search.setOnClickListener({
            //키워드 없으면
            if (edit_search.text.isEmpty()){
                return@setOnClickListener
            }

            //레이아웃매니저 설정
            recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            recyclerView.setHasFixedSize(true)
            //API
            fetchJson(edit_search.text.toString())

            //키보드를 내린다.
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(edit_search.windowToken, 0)

        })
    }

    fun fetchJson(vararg p0 : String){
        val text = URLEncoder.encode("${p0[0]}", "UTF-8")
        println(text)

        val url = URL("https://openapi.naver.com/v1/search/encyc.json?query=${text}&display=10&start=1&genre=")

        val formBody = FormBody.Builder()
            .add("query", "${text}")
            .add("display", "10")
            .add("start", "1")
            .add("genre", "1")
            .build()

        val request = Request.Builder()
            .url(url)
            .addHeader("X-Naver-Client-Id", clientId)
            .addHeader("X-Naver-Client-Secret", clientSecret)
            .method("GET", null)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                println("Success to execute request : $body")

                //Gson을 Kotlin에서 사용 가능한 object로 만든다.
                val gson = GsonBuilder().create()
                //아! 이렇게 하는구나
                val homefeed = gson.fromJson(body, Homefeed::class.java)
                println(homefeed)

                //어답터를 연결하자. 메인쓰레드 변경하기 위해 이 메소드 사용
                runOnUiThread {
                    runOnUiThread {
                        recyclerView.adapter = CustomAdapter(homefeed)
                        edit_search.setText("")
                    }
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute request")
            }
        })

    }

}

data class Homefeed (val items : List<Item>)
data class Item(
    val title : String,
    val description : String
)
