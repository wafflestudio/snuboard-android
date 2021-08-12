package com.wafflestudio.snuboard.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Insets
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.wafflestudio.snuboard.R
import com.wafflestudio.snuboard.data.room.NoticeNoti
import com.wafflestudio.snuboard.di.SharedPreferenceConst
import com.wafflestudio.snuboard.domain.model.*
import com.wafflestudio.snuboard.presentation.TagListAdapter
import com.wafflestudio.snuboard.presentation.department.CollegeDepartmentListAdapter
import com.wafflestudio.snuboard.presentation.department.FilterNoticeListAdapter
import com.wafflestudio.snuboard.presentation.department.FilterSearchNoticeListAdapter
import com.wafflestudio.snuboard.presentation.department.FollowingDepartmentListAdapter
import com.wafflestudio.snuboard.presentation.info.NoticeNotiListAdapter
import com.wafflestudio.snuboard.presentation.notice.FileListAdapter
import com.wafflestudio.snuboard.presentation.notice.NoticeListAdapter


@SuppressLint("RtlHardcoded")
@BindingAdapter("drawer_open")
fun bindDrawerOpen(view: DrawerLayout, bool: Boolean) {
    if (bool)
        view.openDrawer(Gravity.LEFT)
    else
        view.close()
}

@BindingAdapter("heart_filled")
fun bindHeartFilled(view: ImageView, bool: Boolean) {
    if (bool) {
        view.setImageResource(R.drawable.ic_favorite)
        view.imageTintList = ColorStateList
                .valueOf(ContextCompat.getColor(view.context, R.color.tag_selected))
    } else {
        view.setImageResource(R.drawable.ic_favorite_border)
        view.imageTintList = ColorStateList
                .valueOf(ContextCompat.getColor(view.context, R.color.tag_default))
    }
}

@BindingAdapter("notice_items")
fun bindNoticeItems(view: RecyclerView, items: List<Notice>?) {
    val adapt = view.adapter as NoticeListAdapter
    items?.also {
        adapt.submitList(it)
    }
}

@BindingAdapter("filter_notice_items")
fun bindFilterNoticeItems(view: RecyclerView, items: List<Notice>?) {
    val adapt = view.adapter as FilterNoticeListAdapter
    items?.also {
        adapt.items = it
        adapt.notifyDataSetChanged()
    }
}

@BindingAdapter("filter_search_notice_items")
fun bindFilterSearchNoticeItems(view: RecyclerView, items: List<Notice>?) {
    val adapt = view.adapter as FilterSearchNoticeListAdapter
    items?.also {
        adapt.items = it
        adapt.notifyDataSetChanged()
    }
}

@BindingAdapter("notice_noti_items")
fun bindNoticeNotiItems(view: RecyclerView, items: List<NoticeNoti>?) {
    val adapt = view.adapter as NoticeNotiListAdapter
    items?.also {
        adapt.submitList(it)
    }
}

@BindingAdapter("notice_tag_items")
fun bindNoticeTagItems(view: RecyclerView, item: NoticeInterface?) {
    val adapt = view.adapter as TagListAdapter
    item?.also {
        val items = mutableListOf<Tag>(
            Tag(it.departmentName, it.departmentColor)
        )
        items.addAll(
            it.tags.map { it1 ->
                Tag(it1, DepartmentColor.TAG_COLOR)
            }
        )
        adapt.submitList(items)
    }
}

@BindingAdapter("string_tag_items")
fun bindStringTagItems(view: RecyclerView, contents: List<String>?) {
    val adapt = view.adapter as TagListAdapter
    contents?.also {
        adapt.submitList(
            it.map { it1 ->
                Tag(it1, DepartmentColor.TAG_COLOR)
            }
        )
    }
}

@BindingAdapter("notice_noti_tag_items")
fun bindNoticeNotiTagItems(view: RecyclerView, item: NoticeNoti?) {
    val adapt = view.adapter as TagListAdapter
    item?.also {
        val preferenceKey = SharedPreferenceConst.getDepartmentColorKey(item.departmentId)
        val pref = PreferenceManager.getDefaultSharedPreferences(view.context)
        var departmentColorId = pref.getInt(preferenceKey, -1)
        if (departmentColorId == -1) {
            pref.edit {
                putInt(preferenceKey, DepartmentColor.POMEGRANATE.colorId)
            }
            departmentColorId = DepartmentColor.POMEGRANATE.colorId
        }
        val departmentColor = DepartmentColor.fromColorId(departmentColorId)

        val items = mutableListOf<Tag>(
            Tag(it.departmentName, departmentColor!!)
        )
        items.addAll(
            it.tags.split(";").map { it1 ->
                Tag(it1, DepartmentColor.TAG_COLOR)
            }
        )
        adapt.submitList(items)
    }
}

@BindingAdapter("tag_items")
fun bindTagItems(view: RecyclerView, tags: List<Tag>?) {
    if (view.adapter == null)
        view.adapter = TagListAdapter(null)
    val adapt = view.adapter as TagListAdapter
    tags?.also {
        adapt.submitList(it)
    }
}

@BindingAdapter("college_department_items")
fun bindCollegeDepartmentItems(view: RecyclerView, items: List<CollegeDepartment>?) {
    if (view.adapter == null)
        view.adapter = CollegeDepartmentListAdapter()
    val adapt = view.adapter as CollegeDepartmentListAdapter
    items?.also {
        adapt.submitList(it)
    }
}

@BindingAdapter("following_department_items")
fun bindFollowingDepartmentItems(view: RecyclerView, items: List<FollowingDepartment>?) {
    val adapt = view.adapter as FollowingDepartmentListAdapter
    items?.also {
        adapt.submitList(it)
    }
}

@BindingAdapter("file_items")
fun bindFileItems(view: RecyclerView, items: List<NoticeFile>?) {
    if (view.adapter == null)
        view.adapter = FileListAdapter()
    val adapt = view.adapter as FileListAdapter
    items?.also {
        adapt.submitList(it)
    }
}

@BindingAdapter("bottom_padding")
fun bindRecyclerViewBottomPadding(view: View, padding: Int) {
    val scale = view.resources.displayMetrics.density
    val dpAsPixels: Int = (scale * padding + 0.5f).toInt()
    val topDp = (scale * 5 + 0.5f).toInt()
    view.setPadding(0, topDp, 0, dpAsPixels)
}

@BindingAdapter("tag_color")
fun bindTagColor(view: CardView, colorId: Int) {
    val colorString = String.format("#%08x", ContextCompat.getColor(view.context, colorId))
    view.setCardBackgroundColor(Color.parseColor(colorString))
}

@BindingAdapter("department_color")
fun bindDepartmentColor(view: ConstraintLayout, colorId: Int) {
    val colorString = String.format("#%08x", ContextCompat.getColor(view.context, colorId))
    view.setBackgroundColor(Color.parseColor(colorString))
}

@BindingAdapter("preview_color")
fun bindPreviewColor(view: ImageView, colorId: Int) {
    val colorString = String.format("#%08x", ContextCompat.getColor(view.context, colorId))
    view.setColorFilter(Color.parseColor(colorString))
}

@BindingAdapter("text_style_string")
fun bindTextStyleString(view: TextView, style: String) {
    when (style) {
        "bold" -> view.setTypeface(null, Typeface.BOLD)
        "normal" -> view.setTypeface(null, Typeface.NORMAL)
        else -> throw Error("Not valid style in textView")
    }
}

@BindingAdapter("view_visibility_string")
fun bindVisibilityString(view: View, visibility: String) {
    when (visibility) {
        "visible" -> view.visibility = View.VISIBLE
        "gone" -> view.visibility = View.GONE
        else -> throw Error("Not valid visibility in View")
    }
}

@BindingAdapter("click_listener_constraint_layout")
fun bindClickListenerColor(view: ConstraintLayout, clickListener: () -> Unit) {
    view.setOnClickListener {
        clickListener()
    }
}

@BindingAdapter(
        value = ["click_listener_image_url", "click_listener_image_email"],
        requireAll = false
)
fun bindClickListenerUrl(view: ImageView, url: String?, email: String?) {
    url?.let {
        view.setOnClickListener { it2 ->
            (view.context as Activity).startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
        }
    }
    email?.let {
        view.setOnClickListener { it2 ->
            val clipboard = view.context.getSystemService(CLIPBOARD_SERVICE)
            val clip = ClipData.newPlainText("label", it)
            (clipboard as ClipboardManager).setPrimaryClip(clip)
            Toast.makeText(view.context, "이메일이 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}

@BindingAdapter(value = ["web_view_load_content", "web_view_load_style"], requireAll = false)
fun bindLoadContent(view: WebView, content: String?, style: String? = "") {

    // For webview inside fragment, ignore images
    val width = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        when (view.context) {
            is Activity -> {
                val windowMetrics = (view.context as Activity).windowManager.currentWindowMetrics
                val insets: Insets = windowMetrics.windowInsets
                        .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                ((windowMetrics.bounds.width() - insets.left - insets.right) /
                        Resources.getSystem().displayMetrics.density) - 40
            }
            else -> 0
        }
    } else {
        when (view.context) {
            is Activity -> {
                val displayMetrics = DisplayMetrics()
                @Suppress("DEPRECATION")
                (view.context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
                (displayMetrics.widthPixels / Resources.getSystem().displayMetrics.density) - 40
            }
            else -> 0
        }
    }

    content?.let {
        val parsedHTML =
            """
            <!DOCTYPE html>
            <html lang="ko">
            <head>
            <style type="text/css"> 
            html, body { 
                font-size: 14px;
                width:100%; 
                height: 100%; 
                margin: 0px; 
                padding: 0px; 
                } 
            img {
                height: auto;
                width: ${width}px;
                object-fit: contain;
            }
            """ +
                    style +
                    """
            </style>
            </head>
            <body>
            """ + it + "</body></html>"
        val encodedHtml = Base64.encodeToString(parsedHTML.toByteArray(), Base64.NO_PADDING)
        view.loadData(encodedHtml, "text/html; charset=utf-8", "base64")
    }
}
