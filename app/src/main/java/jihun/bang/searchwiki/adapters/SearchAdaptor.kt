package jihun.bang.searchwiki.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import jihun.bang.searchwiki.R
import jihun.bang.searchwiki.data.WikiModel

class SearchAdaptor : BaseAdapter() {
    var items = listOf<WikiModel>()

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): WikiModel = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ListViewHolder
        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_smmary, parent, false)
            holder = ListViewHolder()
            holder.imageTitle = view.findViewById(R.id.image_title)
            holder.textTitle = view.findViewById(R.id.text_title)
            holder.textSubTitle = view.findViewById(R.id.text_sub_title)
            view.tag = holder
        } else {
            holder = convertView.tag as ListViewHolder
            view = convertView
        }

        val item = items[position]
        item.thumbnail?.let {
            holder.imageTitle?.setImageBitmap(it)
        } ?: kotlin.run {
            holder.imageTitle?.setImageDrawable(null)
        }
        holder.textTitle?.text = item.title
        holder.textSubTitle?.text = item.content

        return view
    }

    class ListViewHolder {
        var imageTitle: ImageView? = null
        var textTitle: TextView? = null
        var textSubTitle: TextView? = null
    }
}