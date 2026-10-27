package com.example.data.repository

import com.example.data.model.MetaItem
import com.example.data.model.StreamItem
import com.example.data.model.VideoEpisode

object DefaultMediaData {

    val sampleMovies = listOf(
        MetaItem(
            id = "tt0063350",
            type = "movie",
            name = "Night of the Living Dead",
            poster = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80",
            background = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=1200&auto=format&fit=crop&q=80",
            description = "A ragtag group of Pennsylvanians barricade themselves in an old farmhouse to remain safe from a bloodthirsty, flesh-eating breed of monsters who are ravaging the Northeast United States.",
            releaseInfo = "1968",
            imdbRating = "7.8",
            genres = listOf("Horror", "Thriller", "Classic"),
            runtime = "96 min"
        ),
        MetaItem(
            id = "tt1375666",
            type = "movie",
            name = "Inception",
            poster = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=600&auto=format&fit=crop&q=80",
            background = "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=1200&auto=format&fit=crop&q=80",
            description = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster.",
            releaseInfo = "2010",
            imdbRating = "8.8",
            genres = listOf("Action", "Adventure", "Sci-Fi"),
            runtime = "148 min"
        ),
        MetaItem(
            id = "tt0816692",
            type = "movie",
            name = "Interstellar",
            poster = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600&auto=format&fit=crop&q=80",
            background = "https://images.unsplash.com/photo-1446776811953-b23d57bd21aa?w=1200&auto=format&fit=crop&q=80",
            description = "When Earth becomes uninhabitable in the future, a farmer and ex-NASA pilot, Joseph Cooper, is tasked to pilot a spacecraft, along with a team of researchers, to find a new planet for humans.",
            releaseInfo = "2014",
            imdbRating = "8.7",
            genres = listOf("Adventure", "Drama", "Sci-Fi"),
            runtime = "169 min"
        ),
        MetaItem(
            id = "tt2454290",
            type = "movie",
            name = "Tears of Steel",
            poster = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=600&auto=format&fit=crop&q=80",
            background = "https://images.unsplash.com/photo-1478760329108-5c3ed9d495a0?w=1200&auto=format&fit=crop&q=80",
            description = "In a dystopian future, a group of warriors and scientists gather at the Oude Kerk in Amsterdam to stage a crucial historical event in a desperate attempt to rescue the world from destructive robots.",
            releaseInfo = "2012",
            imdbRating = "7.5",
            genres = listOf("Sci-Fi", "Action", "Short"),
            runtime = "12 min"
        ),
        MetaItem(
            id = "tt1087260",
            type = "movie",
            name = "Big Buck Bunny",
            poster = "https://images.unsplash.com/photo-1574375927938-d5a98e8ffe85?w=600&auto=format&fit=crop&q=80",
            background = "https://images.unsplash.com/photo-1518791841217-8f162f1e1131?w=1200&auto=format&fit=crop&q=80",
            description = "A large and lovable rabbit deals with bullying forest creatures in this groundbreaking open-source animation masterpiece from the Blender Institute.",
            releaseInfo = "2008",
            imdbRating = "8.0",
            genres = listOf("Animation", "Comedy", "Family"),
            runtime = "10 min"
        ),
        MetaItem(
            id = "tt0468569",
            type = "movie",
            name = "The Dark Knight",
            poster = "https://images.unsplash.com/photo-1509347528160-9a9e33742cdb?w=600&auto=format&fit=crop&q=80",
            background = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=1200&auto=format&fit=crop&q=80",
            description = "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.",
            releaseInfo = "2008",
            imdbRating = "9.0",
            genres = listOf("Action", "Crime", "Drama"),
            runtime = "152 min"
        )
    )

    val sampleSeries = listOf(
        MetaItem(
            id = "tt0903747",
            type = "series",
            name = "Breaking Bad",
            poster = "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=600&auto=format&fit=crop&q=80",
            background = "https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=1200&auto=format&fit=crop&q=80",
            description = "A chemistry teacher diagnosed with inoperable lung cancer turns to manufacturing and selling methamphetamine with a former student in order to secure his family's future.",
            releaseInfo = "2008-2013",
            imdbRating = "9.5",
            genres = listOf("Crime", "Drama", "Thriller"),
            videos = listOf(
                VideoEpisode(
                    id = "tt0903747:1:1",
                    title = "Pilot",
                    season = 1,
                    episode = 1,
                    released = "2008-01-20",
                    thumbnail = "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=400&auto=format&fit=crop&q=80",
                    imdbRating = "9.0"
                ),
                VideoEpisode(
                    id = "tt0903747:1:2",
                    title = "Cat's in the Bag...",
                    season = 1,
                    episode = 2,
                    released = "2008-01-27",
                    thumbnail = "https://images.unsplash.com/photo-1507679799987-c73779587ccf?w=400&auto=format&fit=crop&q=80",
                    imdbRating = "8.6"
                ),
                VideoEpisode(
                    id = "tt0903747:1:3",
                    title = "...And the Bag's in the River",
                    season = 1,
                    episode = 3,
                    released = "2008-02-10",
                    thumbnail = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=400&auto=format&fit=crop&q=80",
                    imdbRating = "8.7"
                )
            )
        ),
        MetaItem(
            id = "tt0944947",
            type = "series",
            name = "Game of Thrones",
            poster = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80",
            background = "https://images.unsplash.com/photo-1514539079130-25950c84af65?w=1200&auto=format&fit=crop&q=80",
            description = "Nine noble families fight for control over the lands of Westeros, while an ancient enemy returns after being dormant for millennia.",
            releaseInfo = "2011-2019",
            imdbRating = "9.2",
            genres = listOf("Action", "Adventure", "Drama"),
            videos = listOf(
                VideoEpisode(
                    id = "tt0944947:1:1",
                    title = "Winter Is Coming",
                    season = 1,
                    episode = 1,
                    released = "2011-04-17",
                    thumbnail = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=400&auto=format&fit=crop&q=80",
                    imdbRating = "9.0"
                ),
                VideoEpisode(
                    id = "tt0944947:1:2",
                    title = "The Kingsroad",
                    season = 1,
                    episode = 2,
                    released = "2011-04-24",
                    thumbnail = "https://images.unsplash.com/photo-1514539079130-25950c84af65?w=400&auto=format&fit=crop&q=80",
                    imdbRating = "8.7"
                )
            )
        ),
        MetaItem(
            id = "tt4574334",
            type = "series",
            name = "Stranger Things",
            poster = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&auto=format&fit=crop&q=80",
            background = "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=1200&auto=format&fit=crop&q=80",
            description = "When a young boy vanishes, a small town uncovers a mystery involving secret experiments, terrifying supernatural forces and one strange little girl.",
            releaseInfo = "2016-",
            imdbRating = "8.7",
            genres = listOf("Drama", "Fantasy", "Horror"),
            videos = listOf(
                VideoEpisode(
                    id = "tt4574334:1:1",
                    title = "Chapter One: The Vanishing of Will Byers",
                    season = 1,
                    episode = 1,
                    released = "2016-07-15",
                    thumbnail = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=400&auto=format&fit=crop&q=80",
                    imdbRating = "8.5"
                ),
                VideoEpisode(
                    id = "tt4574334:1:2",
                    title = "Chapter Two: The Weirdo on Maple Street",
                    season = 1,
                    episode = 1,
                    released = "2016-07-15",
                    thumbnail = "https://images.unsplash.com/photo-1508739773434-c26b3d09e071?w=400&auto=format&fit=crop&q=80",
                    imdbRating = "8.8"
                )
            )
        )
    )

    fun getWorkingStreamsFor(id: String, name: String?): List<StreamItem> {
        val titleText = name ?: "Stream"
        return listOf(
            StreamItem(
                title = "$titleText [1080p Web-DL - 60 FPS - AAC 5.1]",
                name = "StreamNova Fast CDN [Direct 1080p]",
                url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                quality = "1080p Ultra HD",
                provider = "Fast Stream CDN",
                isDirectPlayable = true
            ),
            StreamItem(
                title = "$titleText [4K HDR Remux - Dolby Atmos 7.1]",
                name = "Debrid Ultra High Bitrate [Direct 4K]",
                url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                quality = "4K Remux",
                provider = "Real-Debrid Cache",
                isDirectPlayable = true
            ),
            StreamItem(
                title = "$titleText [720p Mobile Optimized - Data Saver]",
                name = "Direct Play [Mobile 720p]",
                url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                quality = "720p HD",
                provider = "Public Mirror",
                isDirectPlayable = true
            ),
            StreamItem(
                title = "$titleText [1080p Multi-Sub English/Spanish/French]",
                name = "Community Torrentio Cache",
                url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                quality = "1080p FHD",
                provider = "Community Cloud",
                isDirectPlayable = true
            )
        )
    }
}
