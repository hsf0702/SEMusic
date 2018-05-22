# SEMusic
仿QQ音乐 APP

Java & Kotlin

# 数据库表结构
- SongListTable
- 歌单表
- [SongListContentProvider](https://github.com/neugaojin/SEMusic/blob/master/app/src/main/java/com/se/music/provider/SongListContentProvider.kt)

| _id    | _name  | _count     | _creator | _create_time | _pic       | _info    |
| ------ | ------ | ---------- | -------- | ------------ | ---------- | -------- |
| id主键 | 歌单名 | 歌曲的数量 | 创建者   | 创建时间     | 歌单封面图 | 歌单信息 |

