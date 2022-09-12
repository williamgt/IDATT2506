package no.ntnu.idatt2506.stud.williagt.descriptions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * A simple [Fragment] subclass.
 * Use the [ImageAndDescription.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageAndDescription : Fragment() {
    private lateinit var descriptions: Array<String>
    //private lateinit var images: TypedArray
    private var currPos = 0
    private lateinit var currView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        descriptions = resources.getStringArray(R.array.descriptions)
        //images = resources.obtainTypedArray(R.array.images)
    }

    fun displayNewImgDsc(i: Int) {
        currPos = i.mod(descriptions.size) //currPos to always be a valid value

        currView.findViewById<TextView>(R.id.desc_img).text = descriptions[currPos]

        currView.findViewById<ImageView>(R.id.game_img).setImageResource(getImageId(currPos))
        /*currView.findViewById<ImageView>(R.id.game_img).setImageResource(images.getResourceId(i, 0))
        images.recycle()*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_image_and_description, container, false)
        currView = view
        //Setting image and description to first and
        /*view.findViewById<ImageView>(R.id.game_img).setImageResource(images.getResourceId(currPos, 0))
        images.recycle()*/
        view.findViewById<ImageView>(R.id.game_img).setImageResource(getImageId(currPos))
        view.findViewById<TextView>(R.id.desc_img).text = descriptions[currPos]
        return view
    }

    fun next() {
        displayNewImgDsc(currPos + 1)
    }

    fun prev() {
        displayNewImgDsc(currPos - 1)
    }

    private fun getImageId(pos: Int): Int {
        return when(pos) {
            0 -> R.drawable.minish_cap
            1 -> R.drawable.leaf_green
            2 -> R.drawable.heart_gold
            3 -> R.drawable.platinum
            4 -> R.drawable.bloodborne
            5 -> R.drawable.dark_souls
            6 -> R.drawable.dark_souls_iii
            7 -> R.drawable.elden_ring
            else -> -1
        }
    }
}