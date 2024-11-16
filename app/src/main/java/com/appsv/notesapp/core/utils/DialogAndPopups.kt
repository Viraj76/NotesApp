package com.appsv.notesapp.core.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.appsv.notesapp.R
import com.appsv.notesapp.core.domain.models.LoggedInUserDetail
import com.appsv.notesapp.databinding.ProgressDialogBinding
import com.appsv.notesapp.databinding.SignInDoneBinding
import com.bumptech.glide.Glide

/**
 * Notes App Dialogs and Popups
 */

private var progressDialog : AlertDialog? = null
private var signInDoneDialog : AlertDialog? = null
private var confirmationDialog: AlertDialog? = null
private var usersPopUpWindow : PopupWindow ? = null



fun Fragment.showSignInWaitDialog(message: String){
    val progress = ProgressDialogBinding.inflate(LayoutInflater.from(requireContext()))
    progress.tvMessage.text = message
    progressDialog   = AlertDialog.Builder(requireContext()).setView(progress.root).setCancelable(false).create()
    progressDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    progressDialog!!.show()
}


fun hideSignInWaitDialog(){
    progressDialog?.dismiss()
}


fun Fragment.showSignInDoneDialog(){
    val done = SignInDoneBinding.inflate(LayoutInflater.from(requireContext()))
    signInDoneDialog   = AlertDialog.Builder(requireContext()).setView(done.root).setCancelable(false).create()
    signInDoneDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    signInDoneDialog!!.show()
}


fun hideSignInDoneDialog(){
    signInDoneDialog?.dismiss()
}


@SuppressLint("UseCompatLoadingForDrawables")
fun Fragment.showConfirmationDialog(
    icon: Int,
    iconTint: Int? = null,
    title: String,
    message: String,
    positiveText: String,
    positiveAction: () -> Unit,
    negativeText: String,
    negativeAction: (() -> Unit)? = null
) {
    val drawable = requireContext().getDrawable(icon)
    iconTint?.let {
        drawable?.setTint(requireContext().getColor(it))
    }

    confirmationDialog = AlertDialog.Builder(requireContext()).apply {
        setIcon(drawable)
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
                val userImage: ImageView = userView.findViewById(R.id.ivUserImage)
                val userName: TextView = userView.findViewById(R.id.tvUserName)
                val userEmail: TextView = userView.findViewById(R.id.tvUserEmail)

                userName.text = user?.displayName
                userEmail.text = user?.id
                Glide.with(this).load(user?.profilePictureUri?.toUri()).into(userImage)

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
        isOutsideTouchable = true
        setBackgroundDrawable(null)
        showAsDropDown(view, 0, 10)
    }
}

fun hideUserPopup(){
    usersPopUpWindow?.dismiss()
}
