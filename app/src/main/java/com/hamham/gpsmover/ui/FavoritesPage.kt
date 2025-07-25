package com.hamham.gpsmover.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hamham.gpsmover.R
import com.hamham.gpsmover.adapter.FavListAdapter
import com.hamham.gpsmover.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesPage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var viewModel: MainViewModel? = null
    private var onFavoriteClick: ((com.hamham.gpsmover.room.Favourite) -> Unit)? = null
    private var onBackClick: (() -> Unit)? = null
    private lateinit var itemTouchHelper: androidx.recyclerview.widget.ItemTouchHelper
    private lateinit var favListAdapter: FavListAdapter

    init {
        LayoutInflater.from(context).inflate(R.layout.fragment_favorites, this, true)
        // تم حذف كود back_arrow بالكامل من هنا
        // Disable setupRecyclerView in FavoritesPage to avoid conflict with MapActivity
        // setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(context)
        
        // Add 3dp spacing between items
        recyclerView.addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: android.graphics.Rect,
                view: View,
                parent: androidx.recyclerview.widget.RecyclerView,
                state: androidx.recyclerview.widget.RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                if (position != androidx.recyclerview.widget.RecyclerView.NO_POSITION) {
                    // Add 3dp bottom margin to all items except the last one
                    if (position < parent.adapter?.itemCount?.minus(1) ?: 0) {
                        outRect.bottom = (3 * context.resources.displayMetrics.density).toInt()
                    }
                }
            }
        })
        
        // Create the adapter first
        favListAdapter = FavListAdapter()
        recyclerView.adapter = favListAdapter

        // Setup drag and drop
        itemTouchHelper = FavListAdapter.createItemTouchHelper(favListAdapter)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        
        // Pass the itemTouchHelper to the adapter
        favListAdapter.setItemTouchHelper(itemTouchHelper)

        favListAdapter.onItemDelete = { favourite ->
            viewModel?.deleteFavourite(favourite)
        }

        favListAdapter.onItemClick = { favourite ->
            onFavoriteClick?.invoke(favourite)
        }
        
        favListAdapter.onItemMove = { fromPosition, toPosition ->
            // Save the new order to database
            val updatedFavorites = favListAdapter.getItems().mapIndexed { index, favourite ->
                favourite.copy(order = index)
            }
            viewModel?.updateFavouritesOrder(updatedFavorites)
        }
        
        android.util.Log.d("FavoritesPage", "RecyclerView setup completed with drag and drop and 3dp spacing")
    }

    fun setViewModel(viewModel: MainViewModel) {
        this.viewModel = viewModel
        // Collect StateFlow for favorites list and update UI immediately
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.allFavList.collect { favorites ->
                updateFavoritesList(favorites)
            }
        }
    }

    fun setOnFavoriteClick(listener: (com.hamham.gpsmover.room.Favourite) -> Unit) {
        this.onFavoriteClick = listener
    }

    fun setOnBackClick(listener: () -> Unit) {
        this.onBackClick = listener
    }

    // loadFavorites() is no longer needed; now using LiveData observer

    private fun updateFavoritesList(favorites: List<com.hamham.gpsmover.room.Favourite>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val emptyCard = findViewById<View>(R.id.emptyCard)
        val emptyTitle = findViewById<TextView>(R.id.emptyTitle)
        val emptyDescription = findViewById<TextView>(R.id.emptyDescription)

        if (favorites.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyCard.visibility = View.VISIBLE
            emptyTitle.text = context.getString(R.string.empty_favorites_title)
            emptyDescription.text = context.getString(R.string.empty_favorites_description)
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyCard.visibility = View.GONE
            favListAdapter.setItems(favorites)
        }
    }
} 