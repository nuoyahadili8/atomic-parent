var startNode;
var endNode;
var nodeproperty = "x,y,text,type,nodeid,attribute,transition";// 需要保存的node属性
// attribute-属性保存

var allscene; // 全局scene对象，jtopo用
var allstage;// 全局stage对象，jtopo用
var timer;// 双击延迟标志位

$(function() {
    setInterval("refreshStateWatch()", 30000);
});

function refreshStateWatch() {
    var jobId = $("#jobId").val();
    var workflowId = $("#workflowId").val();

    if ($('#refreshStateable').val() == "true") {
        $.ajax({
            type : "GET",
            url : ctx + 'oozie/watchJob/getFlowRunJsonByJobId',
            data : {
                jobId : jobId,
                workflowId: workflowId,
                date : new Date()
            },
            success : function(R) {
                $('#scale').val(allscene.scaleX);

                allscene.clear();
                loadnode(R.data);

                allstage.addEventListener("mousewheel", function(e) {
                    var na = navigator.userAgent.toLowerCase();
                    if (/chrome/.test(na) || /msie/.test(na)) {

                        var getmovepx = function() {
                            return px = event.wheelDelta;
                        }
                        var v = getmovepx();
                        if (v > 0 && allscene.scaleX < 2.5) {
                            allstage.zoomOut();
                        } else if (v < 0 && allscene.scaleX > 0.5) {
                            allstage.zoomIn();
                        }
                    }
                    setfontsize();
                });
                edittext();
            }
        });
    }
}

$(window).resize(function() {
    $("#canvas").attr("width", $(window).width() - 40);
    $("#canvas").attr("height", $(window).height() - 60);
});
$('canvas').ready(function(e) {
    $("#canvas").attr("width", $(window).width() - 40);
    $("#canvas").attr("height", $(window).height() - 60);

    var canvas = document.getElementById('canvas');
    var stage = new JTopo.Stage(canvas);
    stage.wheelZoom = 0.85;
    allstage = stage;

    // -----------绘制界面构成start--------------
    var scene = new JTopo.Scene();
    // scene.mode = "drag";
    allscene = scene;
    allstage.add(scene);
    allscene.alpha = 1;

    var flow_json = $("#flowjson").val();
    if (flow_json != null) {
        loadnode(flow_json);

    }
    allstage.addEventListener("mousewheel", function(e) {
        var na = navigator.userAgent.toLowerCase();
        if (/chrome/.test(na) || /msie/.test(na)) {

            var getmovepx = function() {
                return px = event.wheelDelta;
            }
            var v = getmovepx();
            if (v > 0 && allscene.scaleX < 2.5) {
                allstage.zoomOut();
            } else if (v < 0 && scene.scaleX > 0.5) {
                allstage.zoomIn();
            }
        }
        setfontsize();
    });
    edittext();
});

function setfontsize() {
    var fontstr = getCurrFontsize();

    var ele = allscene.childs;
    for (var i = 0; i < ele.length; i++) {
        if (ele[i].elementType == 'node') {
            ele[i].font = fontstr;
        }
    }
}

function getmaxnodeid() {// 得到nodeid最大值
    var temp = 0;
    var ele = allscene.childs;
    for (var i = 0; i < ele.length; i++) {
        if (ele[i].elementType == 'node') {
            if (ele[i].nodeid > temp) {
                temp = ele[i].nodeid;
            }
        }
    }
    return temp;
}// 得到nodeid的最大值

function getCurrFontsize() {
    var fontstr;
    if (allscene.scaleX < 0.5) {
        fontstr = "24px Consolas";
    } else if (allscene.scaleX < 0.7) {
        fontstr = "20px Consolas";
    } else if (allscene.scaleX < 0.9) {
        fontstr = "16px Consolas";
    } else if (allscene.scaleX < 1.1) {
        fontstr = "12px Consolas";
    } else if (allscene.scaleX < 1.4) {
        fontstr = "10px Consolas";
    } else if (allscene.scaleX < 1.7) {
        fontstr = "8px Consolas";
    } else if (allscene.scaleX < 2.0) {
        fontstr = "6px Consolas";
    } else if (allscene.scaleX < 2.5) {
        fontstr = "5px Consolas";
    } else {
        fontstr = "4px Consolas";
    }
    return fontstr;
}

function edittext() {// 节点信息编辑
    // 编辑节点信息开始
    var textfield = $("#jtopo_textfield");

    allscene.dbclick(function(event) {
        $('#refreshStateable').val("false");
        clearTimeout(timer);
        if (event.target == null)
            return;
        if (event.target instanceof JTopo.Link)
            return;
        // if(event.target.type=="start" || event.target.type=="end")return;
        var e = event.target;
        console.log("我点击的节点是" + e.nodeid);
        if (event.target.attribute.actionId == null)
            return;
        else
            infoAction(event.target.attribute.actionId);
        // 通过获取json内的存储信息还原 还原状态信息

    });

    $("#jtopo_textfield").blur(function() {
        textfield[0].JTopoNode.text = textfield.hide().val();
    });
}
function getNewNode(state, txt, x, y, nodeid) {
    var img_url;
    if (state == "OK")
        img_url = ctx + 'images/design/OK.png';
    else if (state == "ERROR")
        img_url = ctx + "images/design/ERROR.png";
    else if (state == "KILLED")
        img_url = ctx + "images/design/KILLED.png";
    else if (state == "RUNNING")
        img_url = ctx + "images/design/RUNNING.png";
    else if (state == "NOTRUNNING")
        img_url = ctx + "images/design/NOTRUNNING.png";

    var newnode = new JTopo.Node(txt);
    newnode.state = state;
    newnode.fontColor = "0,0,0";
    newnode.setImage(img_url);
    newnode.zIndex = 3

    newnode.x = x;
    newnode.y = y;
    if (nodeid != null) {
        newnode.nodeid = getmaxnodeid() + 1;
    }

    // newnode.addEventListener("dbclick", function(event) {
    //     if (event.target.attribute.actionId == null)
    //         return;
    //     else
    //         infoAction(event.target.attribute.actionId);
    // });

    newnode.addEventListener("mouseover", function(nodevent) {

        if (nodevent.target.attribute.status == "OK") {
            nodevent.target.alarm = "成功";
            nodevent.target.alarmFont = 2;
            nodevent.target.alarmColor = '0,255,0';
        } else if (nodevent.target.attribute.status == "ERROR") {
            nodevent.target.alarm = "失败";
            nodevent.target.alarmFont = 1;
            nodevent.target.alarmColor = '255,21,21';
        } else if (nodevent.target.attribute.status == "KILLED") {
            nodevent.target.alarm = "被终止";
            nodevent.target.alarmFont = 1;
            nodevent.target.alarmColor = '255,21,21';
        } else if (nodevent.target.attribute.status == "RUNNING") {
            nodevent.target.alarm = "运行中";
            nodevent.target.alarmFont = 1;
            nodevent.target.alarmColor = '50,140,207';
        } else if (nodevent.target.attribute.status == "NOTRUNNING") {
            nodevent.target.alarm = "未运行";
            nodevent.target.alarmFont = 1;
            nodevent.target.alarmColor = '204,204,204';
        }
        tempnodevent = nodevent.target;
    });
    newnode.addEventListener("mouseout", function() {
        tempnodevent.alarm = null;
    });

    return newnode;
}

//功能函数// 传入两个节点，得到一条连线
function getlink(nodeA, nodeZ) {
    var templink = new JTopo.Link(nodeA, nodeZ);
    templink.lineWidth = 3;
    templink.strokeColor = '204,204,204';
    templink.arrowsRadius = 10;

    return templink;
}
function loadnode(jsontext) {// 读取json

    var jsondata = $.parseJSON(jsontext);// json字符串转化为对象
    allscene.alpha = 1;

    var linkarray = jsondata.linkarray;
    var nodes = jsondata.node;

    for (var i = 0; i < nodes.length; i++) {// 将所有node画出来
        var state = nodes[i].attribute.status;
        var txt = nodes[i].text;
        var x = nodes[i].x;
        var y = nodes[i].y;
        var nodeid = nodes[i].nodeid;

        var newnode = getNewNode(state, txt, x, y, nodeid);

        var nodepropertyarray = nodeproperty.split(',');
        for (var j = 0; j < nodepropertyarray.length; j++) {
            newnode[nodepropertyarray[j]] = nodes[i][nodepropertyarray[j]];
        }

        nodes[i].nodeobj = newnode;

        allscene.add(newnode);
    }

    for (var i = 0; i < linkarray.length; i++) {// 连线
        var node1;
        var node2;
        for (var j = 0; j < nodes.length; j++) {
            if (nodes[j].nodeid == linkarray[i].node1id) {
                node1 = nodes[j].nodeobj;
            }
            if (nodes[j].nodeid == linkarray[i].node2id) {
                node2 = nodes[j].nodeobj;
            }
        }

        var templink;

        templink = getlink(node1, node2);
        if (node2.type == "fail") { //指向失败节点的线用红色
            templink.strokeColor = '255,21,21';
        } else if (node2.attribute.status == "NOTRUNNING" || node1.attribute.status == "NOTRUNNING") {//前后节点有未运行的线灰色
            templink.strokeColor = '204,204,204';
        } else {
            if (node1.transition == null || (node1.transition != '*' && node1.transition != node2.text)) {//前节点路径非后节点 ，主要是switch分支，灰色
                templink.strokeColor = '204,204,204';
            } else {//其余线用蓝色
                templink.strokeColor = '50,140,207';
            }
        }

        if (getnode(linkarray[i].node1id) && getnode(linkarray[i].node2id)) {

            allscene.add(templink);
        }
    }

    var datarule = $('#scale').val();
    if (typeof (datarule) == "undefined" || datarule == null)
        datarule = 1;

    allscene.zoom(datarule, datarule);

    if (datarule == 1) {
        $("#mybody").attr("style", "border:5px solid#777");
    }

    setfontsize();
}

function getnode(nodeid) {// 传入nodeid，得到node
    var ele = allscene.childs;
    for (var i = 0; i < ele.length; i++) {
        if (ele[i].elementType == 'node') {
            if (ele[i].nodeid == nodeid) {
                return ele[i];
            }
        }
    }
    return false;
}// 根据nodeid得到node

function noborder() {
    window.body.style.border = 0;
}

function infoAction(actionId){
    var jobId = $("#jobId").val();
    var workflowId = $("#workflowId").val();
    layer.open({
        type: 2,
        area: ['1000px', '600px'],
        fix: false,
        //不固定
        maxmin: true,
        shade: 0.3,
        title: "流程Action信息("+actionId + ")",
        content: ctx + 'oozie/watchJob/toInfoAction?workflowId='+workflowId+"&jobId="+jobId+"&actionId="+actionId,
        btn: ['关闭'],
        // 弹层外区域关闭
        shadeClose: true,
        cancel: function(index) {
            return true;
        }
    });
}