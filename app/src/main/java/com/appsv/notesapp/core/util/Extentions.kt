package com.appsv.notesapp.core.util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.appsv.notesapp.R
import com.appsv.notesapp.core.domain.models.LoggedInUserDetail
import com.appsv.notesapp.databinding.ProgressDialogBinding
import com.appsv.notesapp.databinding.SignInDoneBinding
import com.bumptech.glide.Glide


var dialog : AlertDialog? = null


fun Fragment.showSignInWaitDialog(message: String){
    val progress = ProgressDialogBinding.inflate(LayoutInflater.from(requireContext()))
    progress.tvMessage.text = message
    dialog   = AlertDialog.Builder(requireContext()).setView(progress.root).setCancelable(false).create()
    dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog!!.show()
}


fun hideSignInWaitDialog(){
    dialog?.dismiss()
}

var doneDialog : AlertDialog? = null

fun Fragment.showSignInDoneDialog(){
    val done = SignInDoneBinding.inflate(LayoutInflater.from(requireContext()))
    doneDialog   = AlertDialog.Builder(requireContext()).setView(done.root).setCancelable(false).create()
    doneDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    doneDialog!!.show()
}


fun hideSignInDoneDialog(){
    doneDialog?.dismiss()
}



private var confirmationDialog: AlertDialog? = null

@SuppressLint("UseCompatLoadingForDrawables")
fun Fragment.showConfirmationDialog(
    icon: Int,
    iconTint: Int? = null, // Optional tint color resource
    title: String,
    message: String,
    positiveText: String,
    positiveAction: () -> Unit,
    negativeText: String,
    negativeAction: (() -> Unit)? = null
) {
    val drawable = requireContext().getDrawable(icon)
    iconTint?.let {
        drawable?.setTint(requireContext().getColor(it)) // Apply tint to the drawable
    }

    confirmationDialog = AlertDialog.Builder(requireContext()).apply {
        setIcon(drawable) // Set the tinted drawable as the icon
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveText) { _, _ ->
            positiveAction()
            hideSignInWaitDialog()
        }
        setNegativeButton(negativeText) { _, _ ->
            negativeAction?.invoke()
            hideSignInWaitDialog()
        }
        setCancelable(false)
    }.create()

    confirmationDialog?.show()
}


fun hideConfirmationDialog() {
    confirmationDialog?.dismiss()
    confirmationDialog = null
}

 var  usersPopUpWindow : PopupWindow ? = null

fun Fragment.showUserPopup(
    view: View,
    userList: List<LoggedInUserDetail?>,
    currentUser : String,
    onUserSelected : (String) -> Unit ={}
) {
    val popupView = LayoutInflater.from(context).inflate(R.layout.popup_user_list, null)
    val llLoggedInUsers: LinearLayout = popupView.findViewById(R.id.llLoggedInUsers)
    val tvNoOtherAccounts : TextView = popupView.findViewById(R.id.tvNoOtherAccount)
    usersPopUpWindow = PopupWindow(
        popupView,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        true
    )

    if(userList.size > 1){
        llLoggedInUsers.visibility = View.VISIBLE
        tvNoOtherAccounts.visibility = View.GONE
        val userContainer: LinearLayout = popupView.findViewById(R.id.userContainer)
        userList.forEach { user ->
            if(user?.id != currentUser){
                val userView = LayoutInflater.from(requireContext()).inflate(R.layout.item_loggedin_users, userContainer, false)

                // Bind user data
                val userImage: ImageView = userView.findViewById(R.id.ivUserImage)
                val userName: TextView = userView.findViewById(R.id.tvUserName)
                val userEmail: TextView = userView.findViewById(R.id.tvUserEmail)

                userName.text = user?.displayName
                userEmail.text = user?.id
                Glide.with(this).load(user?.profilePictureUri?.toUri()).into(userImage)

                // Add click listener for each user
                userView.setOnClickListener {
                    onUserSelected(user?.id!!)
                }

                userContainer.addView(userView)
            }

        }
    }

    else{
        llLoggedInUsers.visibility = View.GONE
        tvNoOtherAccounts.visibility = View.VISIBLE
    }





    usersPopUpWindow?.apply {
        elevation = 10f
        isOutsideTouchable = true // Allow dismissing on outside touch
        setBackgroundDrawable(null) // Transparent background for better styling
        showAsDropDown(view, 0, 10) // Show the popup with margins
    }
}

fun hideUserPopup(){
    usersPopUpWindow?.dismiss()
}
