package com.past.music.entity;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/5/15 22:18
 * 描述：
 * 备注：
 * =======================================================
 */
public class SongListEntity {
    public String id;
    public String name;
    public int count;
    public String creator;
    public String create_time;
    public String list_pic;
    public String info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getList_pic() {
        return list_pic;
    }

    public void setList_pic(String list_pic) {
        this.list_pic = list_pic;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
