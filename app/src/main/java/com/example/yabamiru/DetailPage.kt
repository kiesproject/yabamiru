import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import com.example.yabamiru.Data.*
import com.example.yabamiru.DateFormatter
import com.example.yabamiru.EditPage.EditActivity
import com.example.yabamiru.R
import com.example.yabamiru.RecycleA
import kotlinx.android.synthetic.main.detail_page.*

class DetailPage:AppCompatActivity() {
    lateinit var db: AppDatabase
    lateinit var adapter: RecycleA
    private var id:Long? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_page)
        val button: Button = findViewById(R.id.edit_button)
        button.setOnClickListener{
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("taskId",id)
            startActivity(intent)
        }
        setupList()
        id = intent.getLongExtra("id", 0)
        db = AppDatabase.getDatabase(this)
        if (id != 0L && id != null) {
            db.taskDao().loadTaskAndTaskTagsByTaskId(id!!).observe(this, Observer {
                val task = it?.task
                val tags = it?.taskTags
                task?.let{
                    seekBar.progress = it.weight
                    title_text.setText(it.title)
                    Datetext.setText(DateFormatter.timeToDateStr(it.deadLine) + DateFormatter.timeToTimeStr(it.deadLine))
                    memo_text2.text = (it.memo)
                }
                tags?.let{
                    adapter.setList(it)
                }
            })
        }
    }
    private fun setupList(){
        val recyclerView = findViewById<RecyclerView>(R.id.tag_recyclerView)
        adapter = RecycleA(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}






