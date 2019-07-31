var startNode;
var endNode;
var nodeproperty = "x,y,text,type,nodeid,attribute,retryMax,retryInt,cred";// 需要保存的node属性

var allscene;// 全局scene对象，jtopo用
var allstage;// 全局stage对象，jtopo用
var timer;// 双击延迟标志位

var tmpNode; // link自动连接临时node节点
var currentNode = null;// 右键删除临时储量
var jobQueue = $("#jobQueue").val();
var hiveJdbcUrl = $("#hiveJdbcUrl").val();
var taskId = $("#taskId").val();
var tenant = $("#tenant").val();
var hdfsBaseDir = $("#hdfsBaseDir").val();
var taskName = $("#taskName").val();
var taskNameEn = $("#taskNameEn").val();

/* 画布大小动态调整 */
$(window).resize(function() {
    $("#canvas").attr("width", $(window).width() - 153);
    $("#canvas").attr("height", $(window).height() - 116);

    $("#operaDiv").attr("width", $(window).width() - 153);
    $("#operaDiv").attr("height", $(window).height() - 115);
});
var oldNodeName = "";
$(document).ready(function(e) {

    $("#jtopo_textfield").blur(function() {
        if (nameExists(oldNodeName, $("#jtopo_textfield").val())) {
            layer.alert("有重名节点", {
                skin : 'layer-ext-moon'
            });
            $("#jtopo_textfield").hide();
        } else {
            $("#jtopo_textfield")[0].JTopoNode.text = $("#jtopo_textfield").hide().val();
        }
    });

    /* 画布大小调整 */
    $("#canvas").attr("width", $(window).width() - 153);
    $("#canvas").attr("height", $(window).height() - 116);

    $("#operaDiv").attr("width", $(window).width() - 153);
    $("#operaDiv").attr("height", $(window).height() - 115);

    /* 图标拖动时设置带的参数与图标 */
    var nos = $('.ued-list li');
    nos.each(function() {
        this.ondragstart = function(e) {
            e.dataTransfer.setData("text/plain", this.type);
            e.dataTransfer.setDragImage(this.children[0], 25, 25);
        };
    });

    /* 画布可做为拖动目标 */
    var cans2 = $('#canvas')[0];
    cans2.ondragover = function(event) {
        event.preventDefault();
    };
    /* 画布做为拖动目标时行增加一个节点 */
    cans2.ondrop = function(event) {
        event.preventDefault();
        var data = event.dataTransfer.getData("text/plain");
        // alert(data);
        var x;
        var y;
        var s_x;
        var s_y;
        x = event.pageX;
        y = event.pageY;
        var can_sol = $('canvas');
        s_x = can_sol.offset().left;
        s_y = can_sol.offset().top;

        var location = transXy(x, y, s_x, s_y);
        var cloudNode = addnewnode(data, location);

        allscene.add(cloudNode);
        tmpNode = cloudNode;
        JTopo.layout.layoutNode(allscene, cloudNode, true);

    };

    var canvas = document.getElementById('canvas');
    var stage = new JTopo.Stage(canvas);
    stage.wheelZoom = 0.85;

    allstage = stage;

    var scene = new JTopo.Scene();
    allscene = scene;
    allscene.background = ctx + 'images/design/bg1.png';
    allstage.add(scene);
    allscene.alpha = 1;
    var flow_json = $("#flowjson").val();
    if (flow_json != null && flow_json != "") {
    	loadnode(flow_json);
    } else {
    	startnode = getNewNode('start', 'start', 20, 20, 1);
    	allscene.add(startnode);

    	endnode = getNewNode('end', 'end', 753, 458, 2);

    	allscene.add(endnode);

    	var s_e_link = getlink(startnode, endnode);
    	allscene.add(s_e_link);
    }

    // startnode = getNewNode('start', 'start', 20, 20, 1);
    // allscene.add(startnode);
    //
    // endnode = getNewNode('end', 'end', 753, 458, 2);
    //
    // allscene.add(endnode);
    //
    // var s_e_link = getlink(startnode, endnode);
    // allscene.add(s_e_link);

    allstage.addEventListener("mousewheel", function(e) {
        var na = navigator.userAgent.toLowerCase();
        if (/chrome/.test(na) || /msie/.test(na)) {

            var getmovepx = function() {
                return px = event.wheelDelta;
            }
            var v = getmovepx();
            if (v > 0 && allscene.scaleX < 2) {
                allstage.zoomOut();
            } else if (v < 0 && scene.scaleX > 0.5) {
                allstage.zoomIn();
            }
        }
        setfontsize();
    });

    linkNode();// 绘制图形
    edittext();// 编辑节点信息

});

function transXy(x, y, offsetX, offsetY) {

    var centerNode = allscene.getCenterLocation();

    var oX = centerNode.x - allscene.translateX;
    var oY = centerNode.y - allscene.translateY;

    var pX = x - allscene.translateX - offsetX;
    var pY = y - allscene.translateY - offsetY;

    var newX = (pX - centerNode.x) / allscene.scaleX + oX;
    var newY = (pY - centerNode.y) / allscene.scaleY + oY;

    return {
        x : newX,
        y : newY
    };
}

function setfontsize() {
    var fontstr = getCurrFontsize();

    var ele = allscene.childs;
    for (var i = 0; i < ele.length; i++) {
        if (ele[i].elementType == 'node') {
            ele[i].font = fontstr;
            ele[i].fontColor = "#fff";
        }
    }
}

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

function bridpic() {
    if (allstage.eagleEye.visible == true)
        allstage.eagleEye.visible = false;// 鸟瞰图
    else
        allstage.eagleEye.visible = true;
}
function getNewNode(type, txt, x, y, nodeid) {
    var img_url;
    var tipText;
    if (type == 1)
        img_url = ctx + "images/design/jc/fs.png";
    else if (type == 2)
        img_url = ctx + "images/design/jc/java.png";
    else if (type == 3)
        img_url = ctx + "images/design/jc/mr.png";
    else if (type == 4)
        img_url = ctx + "images/design/fz/choose.png";
    else if (type == 5)
        img_url = ctx + "images/design/fz/fork.png";
    else if (type == 6)
        img_url = ctx + "images/design/fz/join.png";
    else if (type == 7)
        img_url = ctx + "images/design/kz/email.png";
    else if (type == 8) {
        img_url = ctx + "images/design/kz/ftp.png";
        tipText = "1";
    } else if (type == 9)
        img_url = ctx + "images/design/kz/hive.png";
    else if (type == 10)
        img_url = ctx + "images/design/kz/sqoop.png";
    else if (type == 11)
        img_url = ctx + "images/design/kz/zip.png";
    else if (type == 12)
        img_url = ctx + "images/design/jc/pig.png";
    else if (type == 13)
        img_url = ctx + "images/design/kz/shell.png";
    else if (type == 14)
        img_url = ctx + "images/design/kz/ssh.png";
    else if (type == 15)
        img_url = ctx + "images/design/kz/hive2.png";
    else if (type == 16)
        img_url = "images/design/kz/distcp.png";
    else if (type == 17)
        img_url = ctx + "images/design/kz/spark.png";
    else if (type == 18)
        img_url = ctx + "images/design/kz/storm.png";
    else if (type == 19)
        img_url = ctx + "images/design/kz/encrypt.png";
    else if (type == 20)
        img_url = ctx + "images/design/kz/merge.png";
    else if (type == 21)
        img_url = ctx + "images/design/kz/split.png";
    else if (type == 22)
        img_url = ctx + "images/design/kz/sorting.png";
    else if (type == 23)
        img_url = ctx + "images/design/kz/md5.png";
    else if (type == 24)
        img_url = ctx + "images/design/kz/checkfile.png";
    else if (type == 25)
        img_url = ctx + "images/design/kz/webservice.png";
    else if (type == 26)
        img_url = ctx + "images/design/kz/http.png";
    else if (type == 27)
        img_url = ctx + "images/design/kz/sql.png";
    else if (type == 28)
        img_url = ctx + "images/design/kz/load.png";
    else if (type == 29)
        img_url = ctx + "images/design/kz/extract.png";
    else if (type == 30)
        img_url = ctx + "images/design/kz/procedure.png";
    else if (type == 31)
        img_url = ctx + "images/design/fz/childprocess.png";
    else if (type == 32)
        img_url = ctx + "images/design/kz/custom.png";
    else if (type == 33)
        img_url = ctx + "images/design/kz/wash.png";
    else if (type == 34)
        img_url = ctx + "images/design/kz/es.png";
    else if (type == 'impala')
        img_url = ctx + "images/design/kz/impala.png";
    else if (type == 'gbase')
        img_url = ctx + "images/design/kz/gbase.png";
    else if (type == 'start')
        img_url = ctx + "images/design/start.png";
    else if (type == 'end')
        img_url = ctx + "images/design/end.png";

    var newnode = new JTopo.Node(txt);
    newnode.type = type;
    newnode.fontColor = "0,0,0";
    newnode.setImage(img_url);
    newnode.zIndex = 3

    if (tipText != null && tipText > 0) {
        newnode.tipText = tipText;

        newnode.retryMax = tipText;
        newnode.retryInt = 5;
    }
    newnode.x = x;
    newnode.y = y;
    if (newnode.type == 15) {
        initHive2Cred(newnode);
    }

    if (nodeid == null) {
        newnode.nodeid = getmaxnodeid() + 1;
    } else {
        newnode.nodeid = nodeid;
    }

    newnode.addEventListener("mouseup", function(event) {
        clearTimeout(timer);
        if (event.target == null)
            return;
        if (event.target.type == "start" || event.target.type == "end")
            return;
        if (event.button == 2) {
            currentNode = event.target;
            rightmenu(event);// 右键菜单
        }
    });
    newnode.addEventListener("dbclick", function(event) {
        console.log("node dbclick");
        clearTimeout(timer);
        if (event.target == null)
            return;
        if (event.target.type == "start" || event.target.type == "end" || event.target.type == "5" || event.target.type == "6")
            return;

        var et = event.target;
        console.log("我点击的节点是" + et.nodeid);
        console.log("我点击的节点属性是" + et.attribute);
        // 通过获取json内的存储信息还原
        var divtype;
        var titleStr;
        var action;
        var url;
        // console.log("divtype的内容是：" + divtype);
        if (divtype == null) {
            if (et.type == 1) {
                action = 'fs';
                titleStr = 'HDFS(Fs)配置';
                divtype = 'fs_div';
                $.modal.open(titleStr, ctx + 'oozie/action/'+action + "?nodeId=" + et.nodeid,800,700);
            } else if (et.type == 2) {
                action = 'java';
                titleStr = 'Java配置';
                divtype = 'java_div';
                url = ctx + '/oozie/action/'+action + "?nodeId=" + et.nodeid;
                $.modal.open(titleStr, url,800,700);
            } else if (et.type == 3) {
                action = 'mapreduce';
                titleStr = 'MapReduce配置';
                divtype = 'mr_div';
                var attr = et.attribute;
                var mapType = '';
                if (attr != null) {
                    mapType = attr.mapType;
                }else{
                    mapType = '0';
                }
                url = ctx + '/oozie/action/'+action + "?nodeId=" + et.nodeid + "&mapType=" + mapType
                $.modal.open(titleStr, url,800,700);
            } else if (et.type == 4) {
                divtype = 'choose_div';
                action = 'choose';
                titleStr = '分支条件配置';
                var outlink = et.outLinks;
                if(outlink==null || outlink.length<2){
                    layer.alert("请配置至少两个分支！", {
                        skin : 'layer-ext-moon'
                    });
                    return;
                }
                url = ctx + '/oozie/action/'+action + "?nodeId=" + et.nodeid;
                $.modal.open(titleStr, url,800,350);
            } else if (et.type == 7) {
                action = 'email';
                titleStr = 'Email配置';
                divtype = 'email_div';
                url = ctx + '/oozie/action/'+action + "?nodeId=" + et.nodeid;
                $.modal.open(titleStr, url,800,600);
            } else if (et.type == 8) {
                titleStr = 'FTP配置';
                action = 'ftp';
                divtype = 'ftp_div';
                var attr_ftp = et.attribute;
                if (attr_ftp != null && typeof (attr_ftp) != "undefined") {
                    var ftp_ver = attr_ftp.ftp_ver;
                    if (ftp_ver == null || typeof (ftp_ver) == "undefined") {
                        attr_ftp["ftp_div"] = "old";
                    }
                }
            } else if (et.type == 9) {
                action = 'hive';
                titleStr = 'Hive配置';
                divtype = 'hive_div';
            } else if (et.type == 10) {
                action = 'sqoop';
                titleStr = 'Sqoop配置';
                divtype = 'sqoop_div';
                url = ctx + '/oozie/action/'+action + "?nodeId=" + et.nodeid;
                $.modal.open(titleStr, url,800,600);
            } else if (et.type == 11) {
                action = 'zip';
                titleStr = '压缩配置';
                divtype = 'zip_div';
            } else if (et.type == 12) {
                action = 'pig';
                titleStr = 'Pig配置';
                divtype = 'pig_div';
                url = ctx + '/oozie/action/'+action + "?nodeId=" + et.nodeid;
                $.modal.open(titleStr, url,800,600);
            } else if (et.type == 13) {
                action = 'shell';
                titleStr = 'Shell配置';
                divtype = 'shell_div';
                url = ctx + '/oozie/action/'+action + "?nodeId=" + et.nodeid;
                $.modal.open(titleStr, url,800,600);
            } else if (et.type == 14) {
                action = 'ssh';
                titleStr = 'Ssh配置';
                divtype = 'ssh_div';
                url = ctx + '/oozie/action/'+action + "?nodeId=" + et.nodeid;
                $.modal.open(titleStr, url,800,600);
            } else if (et.type == 15) {
                action = 'hive2';
                titleStr = 'Hive2配置';
                divtype = 'hive2_div';
                url = ctx + '/oozie/action/'+action + "?nodeId=" + et.nodeid;
                $.modal.open(titleStr, url,800,700);
            } else if (et.type == 16) {
                action = 'distcp';
                titleStr = 'Hadoop Distcp配置';
                divtype = 'distcp_div';
            } else if (et.type == 17) {
                action = 'spark';
                titleStr = 'Spark配置';
                divtype = 'spark_div';
                url = ctx + '/oozie/action/'+action + "?nodeId=" + et.nodeid;
                $.modal.open(titleStr, url,800,750);
            } else if (et.type == 18) {
                action = 'storm';
                titleStr = 'Storm配置';
                divtype = 'storm_div';
            } else if (et.type == 19) {
                action = 'encrypt';
                titleStr = 'Encode配置';
                divtype = 'encrypt_div';
            } else if (et.type == 20) {
                action = 'merge';
                titleStr = 'Merge配置';
                divtype = 'merge_div';
            } else if (et.type == 21) {
                action = 'split';
                titleStr = 'Split配置';
                divtype = 'split_div';
            } else if (et.type == 22) {
                action = 'sorting';
                titleStr = 'Sorting配置';
                divtype = 'sorting_div';
            } else if (et.type == 23) {
                action = 'md5';
                titleStr = 'Md5配置';
                divtype = 'md5_div';
            } else if (et.type == 24) {
                action = 'checkfile';
                titleStr = 'Checkfile配置';
                divtype = 'checkfile_div';
            } else if (et.type == 25) {
                action = 'webservice';
                titleStr = 'Webservice配置';
                divtype = 'webservice_div';
            } else if (et.type == 26) {
                action = 'http';
                titleStr = 'Http配置';
                divtype = 'http_div';
            } else if (et.type == 27) {
                action = 'sql';
                titleStr = 'Sql配置';
                divtype = 'sql_div';
            } else if (et.type == 28) {
                action = 'load';
                titleStr = 'Load配置';
                divtype = 'load_div';
            } else if (et.type == 29) {
                action = 'extract';
                titleStr = 'Extract配置';
                divtype = 'extract_div';
            } else if (et.type == 30) {
                action = 'procedure';
                titleStr = 'Procedure配置';
                divtype = 'procedure_div';
            } else if (et.type == 31) {
                titleStr = '子流程配置';
                action = 'child';
                divtype = 'childprocess';
                initChild(et);
            } else if (et.type == 33) {
                action = 'wash';
                titleStr = '清洗配置';
                divtype = 'wash_div';
            } else if (et.type == 34) {
                action = 'esload';
                titleStr = 'ElasticsSearch加载配置';
                divtype = 'esload_div';
            } else if (et.type == 'impala') {
                action = 'impala';
                titleStr = 'Impala配置';
                divtype = 'impala_div';
            } else if (et.type == 'gbase') {
                action = 'gbase';
                titleStr = 'Gbase配置';
                divtype = 'gbase_div';
            }
        }
    });

    return newnode;
}


// 设置hive2安全凭证
function initHive2Cred(node) {
    var confCredential_name = $("#confCredential_name").val();
    node.cred = confCredential_name;
}

// 功能函数// 传入两个节点，得到一条连线
function getlink(nodeA, nodeZ) {
    var templink = new JTopo.Link(nodeA, nodeZ);
    templink.lineWidth = 3;
    templink.strokeColor = '204,204,204';
    templink.arrowsRadius = 10;
    templink.addEventListener("drop", function(event) {
        var outnode = event.target.nodeA;
        var innode = event.target.nodeZ;
        allscene.remove(event.target);
        allscene.add(getlink(outnode, tmpNode));
        allscene.add(getlink(tmpNode, innode));
    });
    templink.addEventListener("dbclick", function(event) {
        allscene.remove(event.target);
    });
    return templink;
}
function inandoutonly(node) {// 判断该节点是否满足连接条件（进、出限制）
    if (node.inLinks == null && node.outLinks == null) {
        return "empty";
    } else if (node.inLinks.length == 0 && node.outLinks.length >= 1) {
        return "in";
    } else if (node.inLinks.length >= 1 && node.outLinks.length == 0) {
        return "out";
    } else if (node.inLinks.length == 0 && node.outLinks.length == 0) {// 连接线被清空
        // node重置之后的状态
        return "empty";
    } else {
        return "full";
    }
}
function getassist(positionX, positionY, type, imgurl) {// 获取辅助节点

    var cloudNode = new JTopo.Node('root');
    cloudNode.setLocation(positionX, positionY);
    cloudNode.setImage(imgurl + ".jpg");
    cloudNode.layout = {
        type : 'tree',
        width : 180,
        height : 100
    }
    cloudNode.fontColor = "0,0,0";
    if (type == "5") {// 一进多出
        cloudNode.type = "5";//
        return cloudNode;
    }
    if (type == "6") {// 多进一出
        cloudNode.type = "6";//
        return cloudNode;
    }
}

function linkNode() {// 绘制节点连线
    // ----------------------------
    var beginNode = null;
    // 辅助节点（连线）start
    var tempNodeA = new JTopo.Node('tempA');
    tempNodeA.setSize(1, 1);
    var tempNodeZ = new JTopo.Node('tempZ');
    tempNodeZ.setSize(1, 1);
    // var link = new JTopo.Link(tempNodeA, tempNodeZ);
    var link = getlink(tempNodeA, tempNodeZ);
    // 辅助节点（连线）end
    allscene.mouseup(function(e) {
        // 鼠标右键单击取消 连线
        if (e.button == 2) {
            if (e.target instanceof JTopo.Node) {
            } else {
                $("#contextmenu").hide();
            }
            allscene.remove(link);
            beginNode = null;
            return;
        }
        if (e.target === null) {
            $("#contextmenu").hide();
            return;
        }
        clearTimeout(timer); // 这里加一句是为了兼容 Gecko 的浏览器
        if (e.detail == 2) {
            return;
        }
        timer = setTimeout(function() {
            if (e.target != null && e.target instanceof JTopo.Node) {// 非空操作

                if (e.target.type != 4 && e.target.type != 5 && e.target.type != 6) {// 普通节点
                    if ((beginNode == null && (inandoutonly(e.target) == "out" || inandoutonly(e.target) == "empty")) && e.target.type != "end") {
                        beginNode = e.target;
                        allscene.add(link);
                        tempNodeA.setLocation(e.x, e.y);
                        tempNodeZ.setLocation(e.x, e.y);

                    } else if (beginNode !== e.target && beginNode !== null && e.target.type != "start") {
                        var endNode = e.target;
                        var l = getlink(beginNode, endNode);

                        allscene.add(l);
                        beginNode = null;
                        allscene.remove(link);
                    } else {
                        beginNode = null;
                    }
                }// 普通节点
                else if (e.target.type == 4 || e.target.type == 5) {// 辅助节点 单进多出
                    if (beginNode == null && e.target.type != "end") {
                        beginNode = e.target;
                        allscene.add(link);
                        tempNodeA.setLocation(e.x, e.y);
                        tempNodeZ.setLocation(e.x, e.y);

                    } else if (beginNode !== e.target && beginNode !== null && (inandoutonly(e.target) == "in" || inandoutonly(e.target) == "empty") && e.target.type != "start") {
                        var endNode = e.target;
                        var l = getlink(beginNode, endNode);

                        allscene.add(l);
                        beginNode = null;
                        allscene.remove(link);
                    } else {
                        beginNode = null;
                    }
                }// 辅助节点 单进多出
                else if (e.target.type == 6) {// 辅助节点 多进单出
                    if ((beginNode == null && (inandoutonly(e.target) == "out" || inandoutonly(e.target) == "empty")) && e.target.type != "end") {
                        beginNode = e.target;
                        allscene.add(link);
                        tempNodeA.setLocation(e.x, e.y);
                        tempNodeZ.setLocation(e.x, e.y);

                    } else if (beginNode !== e.target && beginNode !== null && e.target.type != "start") {
                        var endNode = e.target;
                        var l = getlink(beginNode, endNode);

                        allscene.add(l);
                        beginNode = null;
                        allscene.remove(link);
                    } else {
                        beginNode = null;
                    }
                }// 辅助节点 多进单出

            }// 非空操作
            else {
                allscene.remove(link);
            }
        }, 200);
    });

    allscene.mousedown(function(e) {
        if (e.target == null || e.target === beginNode || e.target === link) {
            allscene.remove(link);
            beginNode = null;
        }
    });
    allscene.mousemove(function(e) {
        tempNodeZ.setLocation(e.x, e.y);
    });
}

// 节点信息编辑
function edittext() {
    // 编辑节点信息开始
    var textfield = $("#jtopo_textfield");

    allscene.dbclick(function(event) {
        clearTimeout(timer);
        if (event.target == null)
            return;
    });
    /*
     * $("#jtopo_textfield").blur(function() { textfield[0].JTopoNode.text =
     * textfield.hide().val(); });
     */
}
// 查看生成的xml文件

function lookxml() {
    if ($('canvas').is(":visible")) {
        $('canvas').hide();
        $('#operaDiv').show();
        var resultjson = JSON.stringify(getresultjson());
        var taskId = $("#taskId").val();
        $.modal.loading("请耐心等待...");
        // console.log(resultjson);
        $.ajax({
            type : "POST",
            url : ctx + '/oozie/getWorkFlowXml',
            data : {
                flowJson : resultjson,
                date : new Date(),
                "taskId" : taskId
            },
            dataType : "text",
            success : function(data) {
                $("#xmlTxtAreaId").val(data);
                showXmlThemeTextArea(data);
                $.modal.closeLoading();
            }
        });
    } else {
        $('canvas').show();
        $('#operaDiv').hide();
    }
}
var editor_one;
function showXmlThemeTextArea(data) {
    if (editor_one == undefined){
        editor_one = CodeMirror.fromTextArea(document.getElementById("xmlTxtAreaId"), {
            lineNumbers: true,
            matchBrackets: true,
            styleActiveLine: true,
            readOnly: true,
            mode: 'xml'
        });
    } else {
        editor_one.getDoc().setValue(data);
        editor_one.refresh();
    }
}

function valid() {
    var resultjson = JSON.stringify(getresultjson());
    var taskId = $("#taskId").val();
    $.ajax({
        type : "POST",
        url : ctx + '/oozie/validFlow',
        data : {
            flowJson : resultjson,
            date : new Date(),
            "taskId" : taskId
        },
        dataType : "text",
        success : function(data) {
            layer.alert(data, {
                skin : 'layer-ext-moon'
            });
        }
    });

}
// 获取需要保存的json字符串
function getresultjson() {

    var ees = allscene.childs;
    var nodejson = new Array();
    var linkjson = new Array();
    var tempnode;

    var nodepropertyarray = nodeproperty.split(',');

    for (var i = 0; i < ees.length; i++) {
        var ele = ees[i];

        if (ele["elementType"] == "node") {
            tempnode = new Object();
            for (var j = 0; j < nodepropertyarray.length; j++) {
                tempnode[nodepropertyarray[j]] = ele[nodepropertyarray[j]];
            }
            nodejson.push(tempnode);
        } else if (ele["elementType"] == "link") {
            linkjson.push({
                node1id : ele.nodeA.nodeid,
                node2id : ele.nodeZ.nodeid
            });
        }
    }

    return {
        node : nodejson,
        linkarray : linkjson
    };
}
// 读取json
function loadnode(jsontext) {
    allscene.alpha = 1;
    var jsondata = $.parseJSON(jsontext);// json字符串转化为对象
    if (jsondata != null) {
        var linkarray = jsondata.linkarray;
        var nodes = jsondata.node;

        for (var i = 0; i < nodes.length; i++) {// 将所有node画出来
            var type = nodes[i].type;
            var txt = nodes[i].text;
            var x = nodes[i].x;
            var y = nodes[i].y;
            var nodeid = nodes[i].nodeid;

            var newnode = getNewNode(type, txt, x, y, nodeid);

            var tipText = nodes[i].retryMax;
            if (tipText != null)
                newnode.tipText = tipText;

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

            if (getnode(linkarray[i].node1id) && getnode(linkarray[i].node2id)) {

                allscene.add(templink);
            }
        }
    }
}

// 添加node 设置必要属性
function addnewnode(type, location) {
    var name;

    var type_m = type;
    if (type_m == 1)
        name = "fs";
    else if (type_m == 2)
        name = "java";
    else if (type_m == 3)
        name = "mr";
    else if (type_m == 4)
        name = "choose";
    else if (type_m == 5)
        name = "fork";
    else if (type_m == 6)
        name = "join";
    else if (type_m == 7)
        name = "email";
    else if (type_m == 8)
        name = "ftp";
    else if (type_m == 9)
        name = "hive";
    else if (type_m == 10)
        name = "sqoop";
    else if (type_m == 11)
        name = "zip";
    else if (type_m == 12)
        name = "pig";
    else if (type_m == 13)
        name = "shell";
    else if (type_m == 14)
        name = "ssh";
    else if (type_m == 15)
        name = "hive2";
    else if (type_m == 16)
        name = "distcp";
    else if (type_m == 17)
        name = "spark";
    else if (type_m == 18)
        name = "storm";
    else if (type_m == 19)
        name = "encrypt";
    else if (type_m == 20)
        name = "merge";
    else if (type_m == 21)
        name = "split";
    else if (type_m == 22)
        name = "sorting";
    else if (type_m == 23)
        name = "md5";
    else if (type_m == 24)
        name = "checkfile";
    else if (type_m == 25)
        name = "webservice";
    else if (type_m == 26)
        name = "http";
    else if (type_m == 27)
        name = "sql";
    else if (type_m == 28)
        name = "load";
    else if (type_m == 29)
        name = "extract";
    else if (type_m == 30)
        name = "procedure";
    else if (type_m == 31)
        name = "subflow";
    else if (type_m == 32)
        name = "custom";
    else if (type_m == 33)
        name = "wash";
    else if (type_m == 34)
        name = "esload";
    else if (type_m == "impala")
        name = "impala";
    else if (type_m == "gbase")
        name = "gbase";

    var num = getnodeNum(type_m) + 1;
    var thisname = name + num;
    while (nameExists("", thisname)) {
        num++;
        thisname = name + num;
    }

    var cloudNode = getNewNode(type, thisname, location.x, location.y, null);

    cloudNode.font = getCurrFontsize();
    return cloudNode;
}// 添加node 设置必要属性

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

function getnodeNum(type_m) {// 得到同类型的节点数量
    var temp = 0;
    var ele = allscene.childs;
    for (var i = 0; i < ele.length; i++) {
        if (ele[i].elementType == 'node') {
            if (ele[i].type == type_m) {
                temp++;
            }
        }
    }
    return temp;
}

function save() {
    var resultjson = JSON.stringify(getresultjson());
    var taskId = $("#taskId").val();
    $.modal.loading("校验xml，并保存信息中...");
    $.ajax({
        type : "POST",
        url : ctx + '/oozie/saveWorkflow',
        timeout : 30000,
        data : {
            flowJson : resultjson,
            date : new Date(),
            taskId : taskId
        },
        success : function(R) {
            if (R.data == 'success'){
                $.modal.msgSuccess("设计流程保存成功！");
                $.modal.closeLoading();
            }
        },
        error : function(data, textStatus, errorThrown) {
            $.modal.alertError("流程保存异常！");
            $.modal.closeLoading();
        }
    });
}
function saveQuit() {
    var resultjson = JSON.stringify(getresultjson());
    var taskId = $("#taskId").val();
    $.modal.loading("校验xml，并保存信息中...");
    $.ajax({
        type : "POST",
        url : ctx + '/oozie/saveWorkflow',
        timeout : 30000,
        data : {
            flowJson : resultjson,
            date : new Date(),
            taskId : taskId
        },
        success : function(R) {
            if (R.data == 'success'){
                $.modal.msgSuccess("设计流程保存成功！");
                $.modal.closeLoading();
                closeItem();
            }
        },
        error : function(data, textStatus, errorThrown) {
            $.modal.alertError("流程保存异常！");
            $.modal.closeLoading();
        }
    });
}

/** 关闭选项卡 */
var closeItem = function(){
    var topWindow = $(window.parent.document);
    var panelUrl = window.frameElement.getAttribute('data-panel');
    $('.page-tabs-content .active i', topWindow).click();
    if($.common.isNotEmpty(panelUrl)){
        $('.menuTab[data-id="' + panelUrl + '"]', topWindow).addClass('active').siblings('.menuTab').removeClass('active');
        $('.mainContent .Atomic_iframe', topWindow).each(function() {
            if ($(this).data('id') == panelUrl) {
                $(this).show().siblings('.Atomic_iframe').hide();
                return false;
            }
        });
    }
}

function remove_test() {
    allscene.clear();
}

// 根据nodeid得到node
function getnode(nodeid) {
    var ele = allscene.childs;
    for (var i = 0; i < ele.length; i++) {
        if (ele[i].elementType == 'node') {
            if (ele[i].nodeid == nodeid) {
                return ele[i];
            }
        }
    }
    return false;
}

// 将json里面的信息装换成layer需要的html字符串记性显示
function jsontohtml(enode, divtype) {
    var tempenode = enode;

    // $("#ftp_div").attr("data-node",tempnode.nodeid);
    subObj(tempenode, divtype);
}

// 组件与程序包关联写入json串
function writeInputHiddenDom(th, divid) {
    var n = $(th).parent().parent().parent();
    var programPath = "";
    var programType = "";
    n.find(":input").each(function(i, inp) {
        programPath = $(inp).val();
        if ($(inp).attr("id") == "hive2_script") {
            programType = "hive2";
        }
        if ($(inp).attr("id") == "mapreduce_jar") {
            programType = "mr";
        }
        if ($(inp).attr("id") == "spark_jar") {
            programType = "spark";
        }
        if ($(inp).attr("id") == "java_jar") {
            programType = "java";
        }
        $.ajax({
            async : false,
            type : "GET",
            url : ctx + 'application/getFileInfo',
            dataType : "json",
            timeout : 30000,
            data : {
                programPath : programPath,
                date : new Date()
            },
            success : function(data) {
                if (data.fileinfo != null) {
                    if (programType == "hive2") {
                        $("#programId_hive").val(data.fileinfo.id);
                    }
                    if (programType == "mr") {
                        $("#programId_mr").val(data.fileinfo.id);
                    }
                    if (programType == "spark") {
                        $("#programId_spark").val(data.fileinfo.id);
                    }
                    if (programType == "java") {
                        $("#programId_java").val(data.fileinfo.id);
                    }
                }
            }
        });
    });
}

// 将页面内容的信息保存至json流中
function htmltojson(th, divid) {
    console.log("in htmltojson");
    var nodeid = th.attr("data-node");
    var tempnode = new Object();
    // 组件与程序包关联写入json串
    // writeInputHiddenDom(th, divid);
    th.find(":input").each(function(i, inp) {
        if (typeof ($(inp).attr("name")) != "undefined") {
            if ($(inp).attr("type") == "radio") {
                if (inp.checked == true) {
                    tempnode[$(inp).attr("name")] = $(inp).val();
                }
            } else if ($(inp).attr("type") != "button") {
                var txtVal = $(inp).val();
                if ($(inp).attr("name") == "command") {
                    txtVal = txtVal.replace(/\ /g, ";");
                }

                var obj = tempnode[$(inp).attr("name")];
                if (obj == null || typeof (obj) == "undefined") {
                    tempnode[$(inp).attr("name")] = txtVal;
                } else if ($.isArray(obj)) {
                    obj.push(txtVal);
                } else {
                    var newObj = new Array();
                    newObj.push(obj);
                    newObj.push(txtVal);

                    tempnode[$(inp).attr("name")] = newObj;
                }
            }
        }
    });

    var ele = allscene.childs;
    for (var i = 0; i < ele.length; i++) {
        if (ele[i].elementType == 'node') {
            if (ele[i].nodeid == nodeid) {
                ele[i].attribute = tempnode;
                th.find(":input").each(function(i, inp) {
                    if ($(inp).attr("id") != "sChildFlowNameEn")
                        $(inp).val("");
                });
                return;
            }
        }
    }
}


// 遍历object方法
function subObjmin(obj) {
    $.each(obj, function(key, val) {
        if ($.isPlainObject(val) || $.isArray(val)) {
            subObj(val);
        } else {
            // alert(key + '=' + val);
        }
    });
}
// 缩小
function zoomIn() {
    if (allscene.scaleX > 0.5) {
        allstage.zoomIn();
        setfontsize();
    }
}
// 放大
function zoomOut() {
    if (allscene.scaleX < 2) {
        allstage.zoomOut();
        setfontsize();
    }
}
// 鸟瞰
function reduction() {
    allscene.zoom(1, 1);
    setfontsize();
}
// 右键菜单
function rightmenu(event) {
    $("#contextmenu").css({
        top : event.pageY,
        left : event.pageX
    }).show();
}
// 处理删除节点后节点连线的问题
function subnodelink(node) {
    var temnode = node;
    if (node.inLinks != null && node.inLinks.length == 1 && node.outLinks != null && node.outLinks.length == 1) {
        var AnodeTemp = node.inLinks[0].nodeA;
        var ZnodeTemp = node.outLinks[0].nodeZ;
        var newlink = getlink(AnodeTemp, ZnodeTemp);
        allscene.add(newlink);
    }
}

function deleteNode(node) {
    subnodelink(node);
    allscene.remove(node);
}
function renameNode(node) {
    var textfield = $("#jtopo_textfield");
    textfield.css({
        "top" : event.pageY,
        "left" : event.pageX - node.width / 2
    }).show().val(node.text).focus().select();
    textfield[0].JTopoNode = node;
    oldNodeName = textfield.val();
}
function nameExists(oldName, newName) {
    if (oldName == newName) {
        return false;
    } else {
        var ele = allscene.childs;
        for (var i = 0; i < ele.length; i++) {
            if (ele[i].elementType == 'node') {
                if (ele[i].text == newName) {
                    return true;
                }
            }
        }
        return false;
    }
}

function retryNode(node) {
    var retryDiv = $("#retryDiv");
    retryDiv.css({
        "top" : event.pageY - 20,
        "left" : event.pageX - node.width / 2 - 20
    }).show();
    var retryNum = $("#retry_num");
    retryNum[0].JTopoNode = node;
    if (node.retryMax != null)
        retryNum.val(node.retryMax);
    else
        retryNum.val('1');
    var retryInt = $("#retry_int");

    if (node.retryInt != null)
        retryInt.val(node.retryInt);
    else
        retryInt.val('10');

    $("#hive2CredBox").attr("checked",false);
    $("#hCatCredBox").attr("checked",false);
    $("#hbaseCredBox").attr("checked",false);

    var creds=""
    if (node.cred != undefined && node.cred != ''){
        console.log(node.cred);
        creds=node.cred.split(',');
        for(var i=0;i<creds.length;i++){
            if (creds[i] == 'hive2_credential'){
                $('#hive2CredBox').click();
            }
            if (creds[i] == 'hcat_credential'){
                $('#hCatCredBox').click();
            }
            if (creds[i] == 'hbase'){
                $('#hbaseCredBox').click();
            }
        }
    }

    var cred = $("#cred");

    if (node.cred != null)
        cred.val(node.cred);
    else
        cred.val('');

    retryNum.focus().select();
}

function retryOk(node) {
    var retryNum = $("#retry_num");
    if (retryNum.val() > 0) {
        retryNum[0].JTopoNode.retryMax = retryNum.val();
        retryNum[0].JTopoNode.retryInt = $("#retry_int").val();

        retryNum[0].JTopoNode.tipText = retryNum.val();
    } else {
        retryNum[0].JTopoNode.tipText = "";
    }

    var credStr = "";

    if ($("#hive2CredBox").prop("checked")){
        credStr += "hive2_credential,"
    }
    if ($("#hCatCredBox").prop("checked")){
        credStr += "hcat_credential,"
    }
    if ($("#hbaseCredBox").prop("checked")){
        credStr += "hbase,"
    }
    credStr=credStr.substr(0,credStr.lastIndexOf(","))

    $("#cred").val(credStr);
    if (credStr != '') {
        retryNum[0].JTopoNode.cred = credStr;
    }

    $("#retryDiv").hide();
}
function aclick(ae) {// 右键菜单功能实现
    if (ae == 'delete') {
        deleteNode(currentNode);
    } else if (ae == 'rename') {
        renameNode(currentNode);
    } else if (ae == 'retry') {
        retryNode(currentNode);
    }
    $("#contextmenu").hide();

}// 右键菜单功能
function upload() {
    // $("#uploader").css({left: 800});

    if ($("#uploader").css("left") == "-1000px") {
        $("#uploader").css({
            left : 800
        });
    } else {
        $("#uploader").css({
            left : -1000
        });
    }
}

function closeDesign() {
    window.close();
}

function exportXML() {// 文件下载
    var resultjson = JSON.stringify(getresultjson());
    var taskId = $("#taskId").val();
    var form = $("<form>");// 定义一个form表单
    form.attr("style", "display:none");
    form.attr("target", "");
    form.attr("method", "post");
    form.attr("action", "/oozie/exportXML");
    var input1 = $("<input>");
    input1.attr("type", "hidden");
    input1.attr("name", "data");
    input1.attr("value", (new Date()));
    var input2 = $("<input>");
    input2.attr("type", "hidden");
    input2.attr("name", "taskId");
    input2.attr("value", taskId);
    var input3 = $("<input>");
    input3.attr("type", "hidden");
    input3.attr("name", "resultjson");
    input3.attr("value", resultjson);

    $("body").append(form);// 将表单放置在web中
    form.append(input1);
    form.append(input2);
    form.append(input3);

    form.submit();// 表单提交
}// 文件下载
