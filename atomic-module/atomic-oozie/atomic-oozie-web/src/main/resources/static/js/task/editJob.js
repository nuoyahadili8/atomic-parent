function selectSubsystemForOption() {
    $.ajax({
        async : false,
        url : ctx+'oozie/oozieSubsystem/getOozieSubSystemList',
        dataType : 'json',
        timeout : 30000,
        data : {
            date : new Date()
        },
        success : function(R) {
            $("#subsystemId").empty();
            if (R.data.length > 0) {
                $("#subsystemId").append("<option value=''>请选择...</option>");
            }
            $.each(R.data, function(i, item) {
                $("#subsystemId").append("<option value='" + item.id + "'>" + item.nameEn + "【" + item.nameCn + "】" + "</option>");
            });
        }
    });
}

function removeItemParam(dom){
    $(dom).parent().parent().parent().remove();
}

function addParamItem(){
  var html='<tr class="divcss5-d">\n' +
      '\t<td>\n' +
      '\t\t<div class="divcss5-d">\n' +
      '\t\t\t<a href="javascript:;" title="删除" onclick="removeItemParam(this)" style="font-size: 16px;float: right"><i class="fa fa-minus-square-o"></i></a>\n' +
      '\t\t</div>\n' +
      '\t</td>\n' +
      '\t<td class="col-sm-3">\n' +
      '\t\t<div class="divcss5-d">\n' +
      '\t\t\t<input type="text" name="paramKey" class="form-control changeEdit param-key" readonly/>\n' +
      '\t\t</div>\n' +
      '\t</td>\n' +
      '\t<td class="col-sm-8">\n' +
      '\t\t<div class="divcss5-d">\n' +
      '\t\t\t<input type="text" name="paramValue" class="form-control changeEdit param-value" readonly/>\n' +
      '\t\t</div>\n' +
      '\t</td>\n' +
      '</tr>';
    var div =document.querySelector("#item-add-div");
    div.insertAdjacentHTML("beforebegin",html);
    $(".changeEdit").dblclick(function(){
        $(this).removeAttr("readonly");
    }).mouseleave(function(){
        $(this).attr("readonly", "true");
    }).mouseover(function () {
        $(this).removeAttr("readonly");
    });
}

function getOption(tableId,jobId,type){
    var prefix = ctx + "oozie/oozieJobDepedency";
    var params ="";
    var columns = [{
        checkbox: true
    },
        {
            field : 'id',
            title : '',
            visible: false
        },
        {
            field : 'subsystemNameEn',
            title : '子系统',
        },
        {
            field : 'depedencyNameEn',
            title : '依赖作业名称(En)',
        },
        {
            field : 'depedencyNameCn',
            title : '依赖作业名称(Cn)',
        },
        {
            field : 'cycle',
            title : '周期',
        },
        {
            field : 'status',
            title : '运行状态',
        },
        {
            field : 'startTime',
            title : '开始时间',
        },
        {
            field : 'endTime',
            title : '结束时间',
        },
        {
            field : 'durTime',
            title : '运行时长',
        },
        {
            field : 'yarnQueue',
            title : '资源队列',
        },
        {
            field : 'enable',
            title : '是否启用',
            formatter: function (value, row, index) {
                return enableTools(row);
            }
        }];
    if (type == "up"){
        params = {jobId:jobId};

    } else{
        params = {depedencyId: jobId};
        columns.pop();
    }
    var options = {
        id : tableId,
        url: prefix + "/" + type,
        createUrl: prefix + "/add",
        removeUrl: prefix + "/remove",
        modalName: "依赖作业",
        showExport: true,
        queryParams: params,
        columns: columns
    };
    return options;
}

function enableTools(row) {
    if (row.enable != 'on') {
        return '<a href="javascript:;" onclick="enable(\'' + row.id + '\')"><i class=\"fa fa-toggle-off text-info fa-2x\"></i></a> ';
    } else {
        return '<a href="javascript:;" onclick="disable(\'' + row.id + '\')"><i class=\"fa fa-toggle-on text-info fa-2x\"></i></a> ';
    }
}

function disable(id){
    $.modal.confirm("确认要取消此依赖作业吗？", function () {
        $.operate.post(ctx + "oozie/oozieJobDepedency/changeEnable", {"id": id, "enable": "off"},'taskUpDepedency-table');
    });
}

function enable(id){
    $.modal.confirm("确认要取消此依赖作业吗？", function () {
        $.operate.post(ctx + "oozie/oozieJobDepedency/changeEnable", {"id": id, "enable": "on"},'taskUpDepedency-table');
    });
}

function refreshTask(){
    var jobId=$("#jobId").val();
    window.location.href = ctx + "oozie/oozieJob/edit/"+jobId;
}

function getDepedencyUpTaskNumber(){
    var totalRows = $('#spantaskUpDepedency-table').attr("data-totalrows");
    $("#upcounts").text(totalRows);
    var downtotalRows = $('#spantaskDownDepedency-table').attr("data-totalrows");
    $("#downcounts").text(downtotalRows);
}

function saveTask(){
    var keys=$('input[name="paramKey"]');
    var paramKeys=[];
    keys.each(function(){
        paramKeys.push(replaceAllFunction(this.value));
    });

    var values=$('input[name="paramValue"]');
    var paramValues=[];
    values.each(function(){
        paramValues.push(replaceAllFunction(this.value));
    });

    var map = {};
    for (var i=0;i<paramKeys.length;i++){
        map[paramKeys[i]]=paramValues[i];
    }
    var paramJson=JSON.stringify(map);

    var data = $('#form-job-edit').serialize();

    data += "&parameter="+paramJson;

    if ($.validate.form()) {
        var url = prefix + "/edit";
        var config = {
            url: url,
            type: "post",
            dataType: "json",
            data: data,
            beforeSend: function () {
                $.modal.loading("正在处理中，请稍后...");
                $.modal.disable();
            },
            success: function(R) {
                if (R.data == 'success'){
                    $.modal.closeLoading();
                    $.modal.msgSuccess("保存成功！");
                }
            }
        };
        $.ajax(config);
        // $.operate.save(prefix + "/edit", data);
    }
}

/**
 * 双引号替换成单引号
 */
function replaceAllFunction(str){
    var re = new RegExp("\"", "g");
    return str.replace(re, "\'");
}

function replaceAllSh(str) {
    var re = new RegExp("\"", "g");
    return str.replace(re, "\"");
}

