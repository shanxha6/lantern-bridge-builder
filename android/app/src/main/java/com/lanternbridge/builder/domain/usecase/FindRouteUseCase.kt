package com.lanternbridge.builder.domain.usecase

import com.lanternbridge.builder.domain.model.Plank
import com.lanternbridge.builder.domain.model.Post
import com.lanternbridge.builder.domain.model.Route

class FindRouteUseCase {

    fun route(planks: List<Plank>, start: Int, finish: Int): List<Plank>? {
        if (start == finish) return emptyList()
        val previous = HashMap<Int, Int>()
        val visited = explore(planks, start, previous)
        if (!visited.contains(finish)) return null
        return buildChain(previous, start, finish)
    }

    fun bestEffort(posts: List<Post>, planks: List<Plank>, start: Int, finish: Int): Route {
        val direct = route(planks, start, finish)
        if (direct != null) return Route(direct, true)
        val previous = HashMap<Int, Int>()
        val visited = explore(planks, start, previous)
        var farthest = start
        visited.forEach { node ->
            if (posts[node].x > posts[farthest].x) farthest = node
        }
        if (farthest == start) return Route(emptyList(), false)
        return Route(buildChain(previous, start, farthest), false)
    }

    private fun explore(planks: List<Plank>, start: Int, previous: HashMap<Int, Int>): Set<Int> {
        val adjacency = HashMap<Int, MutableList<Int>>()
        planks.forEach { plank ->
            adjacency.getOrPut(plank.from) { mutableListOf() }.add(plank.to)
            adjacency.getOrPut(plank.to) { mutableListOf() }.add(plank.from)
        }
        val visited = LinkedHashSet<Int>()
        visited.add(start)
        val queue = ArrayDeque<Int>()
        queue.add(start)
        while (queue.isNotEmpty()) {
            val node = queue.removeFirst()
            adjacency[node].orEmpty().forEach { other ->
                if (visited.add(other)) {
                    previous[other] = node
                    queue.add(other)
                }
            }
        }
        return visited
    }

    private fun buildChain(previous: Map<Int, Int>, start: Int, target: Int): List<Plank> {
        val chain = ArrayList<Plank>()
        var current = target
        while (current != start) {
            val parent = previous[current] ?: return emptyList()
            chain.add(Plank(parent, current))
            current = parent
        }
        chain.reverse()
        return chain
    }
}
