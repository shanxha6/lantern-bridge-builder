package com.lanternbridge.builder.domain.usecase

import com.lanternbridge.builder.core.config.GameConfig
import com.lanternbridge.builder.domain.model.Plank
import com.lanternbridge.builder.domain.model.Post
import kotlin.math.hypot

class PlacePlankUseCase {

    fun span(a: Post, b: Post): Float = hypot(b.x - a.x, b.y - a.y)

    fun joined(planks: List<Plank>, a: Int, b: Int): Boolean =
        planks.any { (it.from == a && it.to == b) || (it.from == b && it.to == a) }

    fun canPlace(posts: List<Post>, planks: List<Plank>, a: Int, b: Int, planksLeft: Int): Boolean {
        if (a == b || planksLeft <= 0) return false
        if (a !in posts.indices || b !in posts.indices) return false
        if (joined(planks, a, b)) return false
        return span(posts[a], posts[b]) <= GameConfig.MAX_PLACE_SPAN
    }

    fun reachable(posts: List<Post>, origin: Int): Set<Int> {
        if (origin !in posts.indices) return emptySet()
        val source = posts[origin]
        val result = HashSet<Int>()
        posts.forEachIndexed { index, post ->
            if (index != origin && span(source, post) <= GameConfig.MAX_PLACE_SPAN) result.add(index)
        }
        return result
    }
}
