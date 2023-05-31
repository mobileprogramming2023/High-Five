package com.seoultech.mobileprogramming.high_five.qrCode

class sendHtmlTextToWebView {
    fun userQrEncode(signedInUser: String): String {

        /*
        returns the html code for the QR code
        the QR code is generated using the user's uid
        Referenced webView_user_qr_page.html in app\src\main\assets
        */
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
                "            text: \"$signedInUser\",\n" +
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