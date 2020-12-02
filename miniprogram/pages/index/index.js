//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    motto: '健康の為に歩きましょう！',
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },
  //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  onLoad: function () {
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse){
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      wx.getUserInfo({
        success: res => {
          app.globalData.userInfo = res.userInfo
          this.setData({
            userInfo: res.userInfo,
            hasUserInfo: true
          })
        }
      })
    }
  },
  getUserInfo: function(e) {
    console.log(e)
    app.globalData.userInfo = e.detail.userInfo
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  },
  getHosu: function(){
    // 调用云函数
    wx.getWeRunData({
      success(res) {
        // 拿 encryptedData 到开发者后台解密开放数据
        const encryptedData = res.encryptedData
        const iv = res.iv
        // 或拿 cloudID 通过云调用直接获取开放数据
        const cloudID = res.cloudID
        // console.log(res)
        // console.log(encryptedData)
        
        wx.navigateTo({
          url: '../getRunData/getRunData?runData=' + encryptedData + '&iv=' + iv
        })
      }
    })

  },

//微信小程序设置一个定时器



  })
  