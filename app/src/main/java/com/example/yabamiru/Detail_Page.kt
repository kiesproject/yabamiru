import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.yabamiru.Data.AppDatabase
import com.example.yabamiru.R
import kotlinx.android.synthetic.main.app_edit.*
import java.util.Observer

class Detail_Page:AppCompatActivity() {
    lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_page)
        val id = intent.getLongExtra("id", 0)
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "database").build()
        if (id != 0L) {
            db.taskDao().getAll().observe(this,Observer{
                var text_title = getString(title)

            })
        }
    }
}





