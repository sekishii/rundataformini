const app = getApp()

Page({

  data: {
    runData: '',
    iv: ''
  },

  onLoad: function (options) {
    let that = this
    let jsCode = wx.getStorageSync('code')
    console.log(jsCode)
    
    if (options.runData && options.iv) {
      wx.request({
        url: 'http://localhost:8080/rundata',
        method : 'post',
        data: {
          'encodeRunData': options.runData,
          'jsCode': jsCode,
          'iv': options.iv
        },
        header: {
          'content-type': 'application/json'
        },
        success: function(res) {
          console.log(res)
          that.setData({
            runData: res.data.decodeRunData
          })
        }
      })
    }
    
  },

})