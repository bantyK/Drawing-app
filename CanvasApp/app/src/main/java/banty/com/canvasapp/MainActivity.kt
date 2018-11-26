package banty.com.canvasapp

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

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

            R.id.action_undo -> {
                drawingView.undoDrawing()
            }

            R.id.action_redo -> {
                drawingView.redoDrawing()
            }

            R.id.action_save -> {
                showSaveDrawingDialog()
            }
        }
        return false
    }

    fun showSaveDrawingDialog() {
        //save drawing attach to Notification Bar and let User Open Image to share.
        val saveDialog = AlertDialog.Builder(this)
        saveDialog.setTitle("Save drawing")
        saveDialog.setMessage("Save drawing to device Gallery?")
        saveDialog.setPositiveButton(
            "Yes"
        ) { dialog, which -> saveDrawing() }
        saveDialog.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }
        saveDialog.show()
    }

    private fun saveDrawing() {

    }

    fun saveThisDrawing() {
        var path = Environment.getExternalStorageDirectory().toString()
        path = path + "/" + getString(R.string.app_name)
        val dir = File(path)
        //save drawing
        drawingView.setDrawingCacheEnabled(true)

        //attempt to save
        val imTitle = "Drawing" + "_" + System.currentTimeMillis() + ".png"
        val imgSaved = MediaStore.Images.Media.insertImage(
            contentResolver, drawingView.getDrawingCache(),
            imTitle, "a drawing"
        )

        try {
            if (!dir.isDirectory || !dir.exists()) {
                dir.mkdirs()
            }
            drawingView.isDrawingCacheEnabled = true
            val file = File(dir, imTitle)
            val fOut = FileOutputStream(file)
            val bm = drawingView.getDrawingCache()
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut)


        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Uh Oh!")
            alert.setMessage("Oops! Image could not be saved. Do you have enough space in your device?1")
            alert.setPositiveButton("OK", null)
            alert.show()

        } catch (e: IOException) {
            val unsavedToast = Toast.makeText(
                applicationContext,
                "Oops! Image could not be saved. Do you have enough space in your device2?", Toast.LENGTH_SHORT
            )
            unsavedToast.show()
            e.printStackTrace()
        }

        if (imgSaved != null) {
            val savedToast = Toast.makeText(
                applicationContext,
                "Drawing saved to Gallery!", Toast.LENGTH_SHORT
            )
            savedToast.show()
        }

        drawingView.destroyDrawingCache()
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
