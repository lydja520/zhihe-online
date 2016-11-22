/**
 * Created by ShenYunjie on 2015/11/19.
 */
$(function () {
    var uploader = Qiniu.uploader({
        runtimes: 'html5,flash,html4',    //上传模式,依次退化
        browse_button: 'pickfiles',       //上传选择的点选按钮，**必需**
        uptoken_url: '/qiniu/api/image/uptoken',
        //Ajax请求upToken的Url，**强烈建议设置**（服务端提供）
        // uptoken : '<Your upload token>',
        //若未指定uptoken_url,则必须指定 uptoken ,uptoken由其他程序生成
        // unique_names: true,
        // 默认 false，key为文件名。若开启该选项，SDK会为每个文件自动生成key（文件名）
        save_key: false,
        // 默认 false。若在服务端生成uptoken的上传策略中指定了 `sava_key`，则开启，SDK在前端将不对key进行任何处理
        domain: 'http://7xofn0.com1.z0.glb.clouddn.com/',
        //bucket 域名，下载资源时用到，**必需**
        container: 'uploader-dialog',           //上传区域DOM ID，默认是browser_button的父元素，
        max_file_size: '100mb',           //最大文件体积限制
        flash_swf_url: '/static/core/plupload/Moxie.swf',  //引入flash,相对路径
        max_retries: 3,                   //上传失败最大重试次数
        dragdrop: false,                   //开启可拖曳上传
        drop_element: '#uploader-dialog',        //拖曳上传区域元素的ID，拖曳文件或文件夹后可触发上传
        chunk_size: '4mb',                //分块上传时，每片的体积
        auto_start: false,                 //选择文件后自动上传，若关闭需要自己绑定事件触发上传
        multi_selection: false,
        init: {
            'FilesAdded': function (up, files) {
                if (files.length > 1) {
                    files.splice(1, 1000);   //一次只允许添加一个文件
                }
                plupload.each(files, function (file) {
                    $('#progress').html(file.name);
                });
            },
            'BeforeUpload': function (up, file) {
                // 每个文件上传前,处理相关的事情
                console.log('BeforeUpload');
            },
            'UploadProgress': function (up, file) {
                // 每个文件上传时,处理相关的事情
                total = up.total.size;
                loaded = up.total.loaded;
                progress = parseInt(loaded / total * 100);
                $('#progress').css("width", progress + '%');
            },
            'FileUploaded': function (up, file, info) {
                // 每个文件上传成功后,处理相关的事情
                // 其中 info 是文件上传成功后，服务端返回的json，形式如
                // {
                //    "hash": "Fh8xVqod2MQ1mocfI4S4KpRL6D98",
                //    "key": "gogopher.jpg"
                //  }
                // 参考http://developer.qiniu.com/docs/v6/api/overview/up/response/simple-response.html
                // var domain = up.getOption('domain');
                //var res = $.parseJSON(info);
                // var sourceLink = domain + res.key; 获取上传成功后的文件的Url
                console.log(info);
                var _tmp = $('#progress').html("上传完成");
                $('#progress').css("width", "100%");
            },
            'Error': function (up, err, errTip) {
                //上传出错时,处理相关的事情
            },
            'UploadComplete': function () {
                //队列文件处理完毕后,处理相关的事情
            },
            'Key': function (up, file) {
                // 若想在前端对每个文件的key进行个性化处理，可以配置该函数
                // 该配置必须要在 unique_names: false , save_key: false 时才生效
                var _tmp = new Date().getTime();
                var key = _tmp + "" + parseInt((Math.random() + 1) * 10000);
                if (file.name.indexOf('.') > 0) {
                    var suffix = file.name.substring(file.name.indexOf("."), file.name.length);
                    key += suffix;
                }
                return key
            }
        }
    });
    window.uploader = uploader;
})

