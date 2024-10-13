import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.smartchess20.R
import com.example.smartchess20.fragments.DebugFragment
import com.example.smartchess20.fragments.HistoryFragment
import com.example.smartchess20.fragments.LiveFragment

class NavigationAdapter(private val clickListener: (Class<out Fragment>) -> Unit) :
    RecyclerView.Adapter<NavigationAdapter.MenuViewHolder>() {

    private var navigationItems: List<String> = listOf("Live", "History", "Debug", "...", "...", "...", "...")

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTextView: TextView = itemView.findViewById(R.id.navigationItem_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_navigation, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.itemTextView.text = navigationItems[position]
        holder.itemView.setOnClickListener {
            when (position) {
                0 -> clickListener(LiveFragment::class.java)
                1 -> clickListener(HistoryFragment::class.java)
                2 -> clickListener(DebugFragment::class.java)
                else -> { /* Handle other fragments or ignore */ }
            }
        }
    }

    override fun getItemCount(): Int = navigationItems.size
}
