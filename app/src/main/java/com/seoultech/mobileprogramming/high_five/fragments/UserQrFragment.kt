package com.seoultech.mobileprogramming.high_five.fragments

import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.google.firebase.auth.FirebaseAuth
import com.seoultech.mobileprogramming.high_five.databinding.FragmentUserQrBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserQrFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserQrFragment : Fragment() {
    lateinit var binding: FragmentUserQrBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val htmlText = userQrEncode()
        val encodedHtml = Base64.encodeToString(htmlText.toByteArray(), Base64.NO_PADDING)

        binding = FragmentUserQrBinding.inflate(inflater, container, false)


        // Renders QR code through the webView, implemented in html file
        var webViewQrBinding: WebView = binding.wvQrGenerated

        webViewQrBinding.settings.javaScriptEnabled = true
        webViewQrBinding.loadData(
            encodedHtml, "text/html", "base64"
        )

        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserQrFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = UserQrFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }

    private fun userQrEncode(): String {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        // returns the html code for the QR code
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "\n" +
                "\n" +
                "<meta charset=\"UTF-8\" />\n" +
                "<meta name=\"viewport\" content=\"width=device-width,\n" +
                "\t\t\t\t\t\tinitial-scale=1.0\" />\n" +
                "<title>QR Code</title>\n" +
                "\n" +
                "<style>\n" +
                "    body {\n" +
                "        margin: 5;\n" +
                "        padding: 5;\n" +
                "        background-color: #ffffff;\n" +
                "        font-family: \"Open Sans\", sans-serif;\n" +
                "        width: fit-content;\n" +
                "        height: fit-content;\n" +
                "    }\n" +
                "\n" +
                "    header {\n" +
                "        display: flex;\n" +
                "        flex-direction: flex;\n" +
                "        justify-content: flex-start;\n" +
                "        align-items: flex-start;\n" +
                "    }\n" +
                "</style>\n" +
                "\n" +
                "<body>\n" +
                "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/qrcodejs/1.0.0/qrcode.min.js\">\n" +
                "    </script>\n" +
                "\n" +
                "    <div id=\"qrcode\"></div>\n" +
                "</body>\n" +
                "\n" +
                "<!--\n" +
                "        * Generates QR Code which contains uid.\n" +
                "        * uid is used to identify user.\n" +
                "        * (TBA) Contains a text to used for generating .json file for record to DB\n" +
                "    -->\n" +
                "<script>\n" +
                "    // https://davidshimjs.github.io/qrcodejs/\n" +
                "    var qrCode = new QRCode(\"qrcode\",\n" +
                "        {\n" +
                "            text: \"$userId\",\n" +
                "            width: 150,\n" +
                "            height: 150,\n" +
                "            colorDark: \"#000000\",\n" +
                "            colorLight: \"#ffffff\",\n" +
                "            correctLevel: QRCode.CorrectLevel.H\n" +
                "        });\n" +
                "</script>\n" +
                "\n" +
                "</html>"
    }
}