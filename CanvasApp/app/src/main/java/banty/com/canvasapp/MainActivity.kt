package banty.com.canvasapp

import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var toolbarBottom: Toolbar
    private lateinit var drawingView: CustomView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        inflateBottomToolbar()
        drawingView = findViewById(R.id.custom_view)

    }

    private fun inflateBottomToolbar() {
        toolbarBottom = findViewById<Toolbar>(R.id.toolbar_bottom)
        toolbarBottom.inflateMenu(R.menu.drawing_options_menu)
        toolbarBottom.setOnMenuItemClickListener {
            handleMenuItemClick(it)
        }

    }

    private fun handleMenuItemClick(menuItem: MenuItem?): Boolean {
        when (menuItem?.itemId) {
            R.id.action_erase -> {
                showEraseDialog()
            }
        }
        return false
    }

    private fun showEraseDialog() {
        val deleteDialog = AlertDialog.Builder(this)
        deleteDialog.setTitle(getString(R.string.delete_drawing_heading))
        deleteDialog.setMessage(getString(R.string.delete_drawing_warning))
        deleteDialog.setPositiveButton("Yes") { dialog, _ ->
            drawingView.eraseAll()
            dialog.dismiss()
        }
        deleteDialog.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }
        deleteDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
