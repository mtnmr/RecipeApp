package com.example.recipeapp.data

import androidx.room.*

@Entity
data class ShoppingList(
    @PrimaryKey(autoGenerate = true) val listId: Int = 0,
    val listTitle: String = "買い物リスト"
)

//@Entity(
//    foreignKeys = [ForeignKey(
//        entity = ShoppingList::class,
//        parentColumns = arrayOf("listId"),
//        childColumns = arrayOf("parentId"),
//        onDelete = ForeignKey.CASCADE
//    )]
//)
@Entity
data class ListDetail(
    @PrimaryKey(autoGenerate = true) val detailId: Int = 0,
    val parentId: Int,
    val detailName: String,
    val check: Boolean
)


//data class ShoppingListWithDetails(
//    @Embedded val shoppingList: ShoppingList,
//    @Relation(parentColumn = "listId", entityColumn = "parentId")
//    val details: List<ListDetail>
//)