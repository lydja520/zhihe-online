/**
 * Created by ShenYunjie on 2015/11/23.
 */
function initUploader(param) {
    var uploader = Qiniu.uploader({
        runtimes: 'html5,flash,html4',    //上传模式,依次退化
        browse_button: param.pickbutton || 'pickfiles',       //上传选择的点选按钮，**必需**
        uptoken_url: '/qiniu/api/image/uptoken',   //从本地服务器获取token
        save_key: false,
        domain: 'http://7xofn0.com1.z0.glb.clouddn.com/', //bucket 域名，下载资源时用到，**必需**
        container: param.container || undefined,    //上传区域DOM ID，默认是browser_button的父元素，
        max_file_size: '100mb',           //最大文件体积限制
        flash_swf_url: '/static/core/plupload/Moxie.swf',  //引入flash,相对路径
        max_retries: 3,                   //上传失败最大重试次数
        dragdrop: false,                   //开启可拖曳上传
        //drop_element: '#uploader-dialog',        //拖曳上传区域元素的ID，拖曳文件或文件夹后可触发上传
        chunk_size: '4mb',                //分块上传时，每片的体积
        auto_start: true,                 //选择文件后自动上传，若关闭需要自己绑定事件触发上传
        multi_selection: false,
        x_vars: param.data || {},
        init: {
            'FilesAdded': function (up, files) {
                if (files.length > 1) {
                    files.splice(1, 1000);   //一次只允许添加一个文件
                }
                up.files = files;
                if (param.onFileAdd) {
                    param.onFileAdd(files[0]);
                }
            },
            'BeforeUpload': function (up, file) {
                // 每个文件上传前,处理相关的事情
                if (param.onBeforeUpload) {
                    param.onBeforeUpload(up, file);
                }
            },
            'UploadProgress': function (up, file) {
                // 每个文件上传时,处理相关的事情
                total = up.total.size;
                loaded = up.total.loaded;
                progress = parseInt(loaded / total * 100);
                console.log('uploaded : ' + progress + '%');
                /*if (param.onBeforeUpload) {
                 param.onBeforeUpload(up, file);
                 }*/
                if (param.onProgress) {
                    param.onProgress(progress, loaded, total);
                }
            },
            'FileUploaded': function (up, file, info) {
                if (param.onUploaded) {
                    info = $.parseJSON(info);
                    param.onUploaded(up, file, info);
                }
            },
            'Error': function (up, err, errTip) {
                //上传出错时,处理相关的事情
                if (param.onError) {
                    param.onError(up, err, errTip);
                }
            },
            'UploadComplete': function () {
                //队列文件处理完毕后,处理相关的事情
                if (param.onComplete()) {
                    param.onComplete();
                }
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
    return uploader;
}
