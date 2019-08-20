import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.arch.lifecycle.Observer
import android.text.Editable
import com.example.yabamiru.Data.*
import com.example.yabamiru.R
import kotlinx.android.synthetic.main.detail_page.*

class Detail_Page:AppCompatActivity() {
    lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_page)
        val id = intent.getLongExtra("id", 0)
        val aaa:Long = 10L
        val dateString:String = aaa.toString()
        dateString.invoke(10L)
        db = Room.databaseBuilder(this, AppDatabase::class.java, "database").build()
        if (id != 0L) {
            db.taskDao().getById(id).observe(this, Observer {
                    task: Task? -> title_text.text = task!!.title
            })
            db.taskDao().getById(id).observe(this, Observer {
                    task:Task? -> seekBar.progress = task!!.weight
            })
            db.taskDao().getById(id).observe(this, Observer {
                    task:Task? -> Datetext.text = dateString(task!!.deadLine)
            })
            db.taskTagsDao().getByTaskId(id).observe(this, Observer {
                    tasktags:TaskTags? -> tag_recyclerView = tasktags!!.tagId
            })
            db.taskDao().getById(id).observe(this, Observer {
                task:Task? -> memo_text2.text = task!!.memo
            })


        }
    }

}

private operator fun Any.invoke(deadLine: Long): Editable? {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}






