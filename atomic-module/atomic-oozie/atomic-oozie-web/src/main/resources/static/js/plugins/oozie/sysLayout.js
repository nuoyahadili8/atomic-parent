/**
 * @Filename: sysLayout.js
 * @Description: sysLayout
 * @Version: 1.0.0
 * @Author: wangyq.si-tech
 * @UpdateBy: wangyq 20121206
 * @UpdateBy: wangcw 20130403
 * Copyright (c) 2012-2013 si-tech
 *
 * @example
 *说明：
 *1.布局上：整个div设置相对定位，左侧绝对定位，left:0,top:为上部div的高度，右侧绝对定位，right:0,top为上部div的高度，中间的不能设置浮动，也不设置宽度，margin-left:左边div的宽度+间距+边框等，margin-right：右边div的宽度+间距+边框等
 *2.页面只有中间的div是必须显示的，上下左右4个div可根据实际取舍保留，只要结构中不出现相应的标签即可
 *3.初始化时，对于根据内容适应高度的情况，对左中右div设置高度，必须先确定宽度，然后才能确定高度，
 *4.已经考虑到窗口大小改变时会出现滚动条的问题，窗口尺寸改变时会相应$(window).resize(function(){	_init(models,option);})
 *5.初始高度会有3种情况，100%（以window高），auto（以左中右div的内容自适应,取三者中最大的作为公用高度），还可以配置固定高度（比如设置成500，则上中下div高度+间距，总值为500）
 *
 *

 */
(function($){
    $.fn.UED_sysLayout = function(option){

        var defaults = {
            //初始配置
            height:"100%",//初始高度，100%、auto、300
            isLeftCollapse: true,      //初始化时 左边是否隐藏
            isRightCollapse: true,     //初始化时 右边是否隐藏
            allowLeftCollapse: true,      //是否允许 左边可以隐藏
            allowRightCollapse: true,     //是否允许 右边可以隐藏
            //回调配置
            topHeight: 85,
            bottomHeight: 0,
            leftWidth: 140,
            rightWidth: 0,
            space:1 //间隔
        };

       
		
        function _init(models, option){
            var height = option.height;
			//中间三个div的高度
			var centerH=0;
			var iMarginTop=option.topHeight+option.space;
			if(models.top!==null){
				$(models.top).css({'width':'100%','margin-bottom':option.space+'px','height':option.topHeight+'px'});
			}else{
				$(models.middle).css({'margin-top':0});
				iMarginTop=0;
			}
			if(models.left!==null){
				$(models.left).css({'left':0,'top':iMarginTop+'px','position':'absolute'});
			};
			$(models.middle).css({'margin-left':option.leftWidth+option.space+'px', 'margin-right':option.rightWidth+option.space+'px','margin-bottom':option.space+'px'}).parent().css({'position':'relative'});
			if(models.right!==null){
				$(models.right).css({'right':0,'top':iMarginTop+'px','position':'absolute'});
			};
			if(models.bottom!==null){
				$(models.bottom).css({'width':'100%','float':'left','height':option.bottomHeight+'px' });
			}else{
				$(models.middle).css({'margin-bottom':0});
			}
			
			//先确定宽度，然后才能确定高度值
			var leftH=models.left==null?0:$(models.left).height();
			var centerH=$(models.middle).height();
			var rightH=models.right==null?0:$(models.right).height();
			//iLength用来计算上下间距的个数，如果上div或者下div没有，则只有一个间距，如果上下都没有，则有0个间距，默认是上下都有，
			var iLength;
			if(models.top==null && models.bottom==null){
				iLength=0;
			}else if(models.top==null || models.bottom==null){
				iLength=1;
			}else{
				iLength=2
			}
            if(height=="100%"){
				centerH=$(window).height()-option.topHeight-option.bottomHeight-iLength*option.space-3;
            }else if(height==='auto'){
				centerH=centerH>rightH?centerH:rightH;
				centerH=centerH>leftH?centerH:leftH;
			}else{
				centerH=height-option.topHeight-option.bottomHeight-iLength*option.space-3;
            }
			
			
			$(models.middle).css({'height':centerH+'px'});
			if(models.left!==null){
				$(models.left).css({'width':option.leftWidth+'px','height':centerH+'px'});
				$(models.middle).css({'margin-left':(option.leftWidth+option.space)+'px'});
				//如果左侧可以收缩，添加操作相关的标签，并且修改相关样式
				if(option.allowLeftCollapse){
					/*$(models.middle).parent().append('<div class="layout-collapse-left" style="top:'+ iMarginTop +'px;height:'+centerH+'px;"><div class="layout-collapse-left-toggle"></div></div>');
					$(models.left).prepend('<div class="layout-header-left"><div class="layout-header-toggle-left"></div></div>');*/
					if(option.isLeftCollapse=='true'){
						//初始化时收缩
						$(models.middle).css({'margin-left':($(".layout-collapse-left").width()+option.space+2)+'px'});
					}else{
						//初始化时显示
						
						$(models.middle).css({'margin-right':'0px'});
					}	
				}
			}else{
				$(models.middle).css({'margin-left':0});
			}
        }
		 function fnResize(models, option){
            var height = option.height;
			//中间三个div的高度
			var centerH=0, iLength=2;
			if(models.top==null || models.bottom==null){
				iLength=1;
			}else if(models.top==null && models.bottom==null){
				iLength=0;
			}
            if(height=="100%"){
				centerH=$(window).height()-option.topHeight-option.bottomHeight-iLength*option.space-3;
				$(models.left).css({'height':centerH+'px'});
				$(models.middle).css({'height':centerH+'px'});
				$(models.right).css({'height':centerH+'px'});
            }
            var len = $(models.left).find("h3").length,
            	titHeight = $(models.left).find("h3").height(),
				titAll = len*titHeight,
				leftHeight	= centerH-titAll;
            	$(".accordion-body").css({'height':leftHeight+'px'});
            	// alert(leftHeight)
			
        }
		
        function strToJson(str){
            str = str? str:"";
            return  eval( "({" + str + "})" );
        }

        return this.each(function(i, n){
			var _rel =  strToJson( $(n).attr("data-rel") );
            var models = {};
            models.top = $(n).children("div[position='top']").get(0) || null;
            models.bottom = $(n).children("div[position='bottom']").get(0) || null;
            models.left = $(n).children("div[position='left']").get(0) || null;
            models.right = $(n).children("div[position='right']").get(0) || null;
            models.middle = $(n).children("div[position='center']").get(0) || null;
			if(models.top==null){
				defaults.topHeight=0;
			};
			if(models.left==null){
				defaults.leftWidth=0;
			};
			if(models.right==null){
				defaults.rightWidth=0;
			};
			if(models.bottom==null){
				defaults.bottomHeight=0;
			};
            /* 合并默认参数和用户自定义参数 */
            option = $.extend(true, {}, defaults, option, _rel);
            _init(models,option);
			
			$(window).resize(function(){
				fnResize(models,option);
			});
			
        })
    }

    $(function(){
        $("*[data-role='ued-layout']").UED_sysLayout();
    });
})(jQuery);

(function($){
	$.fn.UED_scroll = function(option){
		var defaults = {
			time:500,                   //默认滚动时间间隔
			num:1,					    //每次滚动数目
			showNum:10,                  //显示数目
			leftBtn:".scrollPrev",		//向左滚动按钮
			rightBtn:".scrollNext",		//向右滚动按钮
			wrap:".ued-scrollWrap",			//滚动条外层对象
			list:".js_scrollList",         //滚动列表
			btnVisible:true,            //当展现项目小于展现区块宽度时，默认显示左右按钮；当为false时，隐藏左右滚动按钮
			flag:0,					//滚动项目数
			itemWidth:0
		};
		function scrollfn(o,opts){
			var _leftBtn = o.find(opts.leftBtn),
				_rightBtn = o.find(opts.rightBtn),
				_wrap = o.find(opts.wrap),
				_list = o.find(opts.list);
			var flag = opts.flag,
				num = opts.num,
				time = opts.time,
				btnVisible = opts.btnVisible,
				showNum = opts.showNum;
			var litem = _list.children();
			var ln = _list.children().length;
			var listW = 0,
				listmgl = 0;
			//判断是否有展现项目个数限制，如果有的话，通过项目数设置展示区宽度
			if(!litem.hasClass("curr")){
				litem.first().addClass("curr");	
			}
			if(showNum>0){
				var wrapW = 0;
				for(var i=0;i<showNum;i++){
					wrapW += litem.eq(i).outerWidth(true);
				}
				_wrap.width(wrapW);
				
			}else{
				_wrap.css("width","100%");
				var wrapW = _wrap.width();
				
			}
			for(var i=0;i<=ln;i++){
				listW += litem.eq(i).outerWidth(true);		
			}
			for(var i=0;i<flag;i++){
				listmgl += litem.eq(i).outerWidth(true);
			}
			listW += 500;
			_list.css({"width":listW+"px"});

						
			if(listW<=wrapW){
				if(btnVisible){
					_leftBtn.addClass("prev-dis");
					_rightBtn.addClass("next-dis");
				}else{
					_leftBtn.add(_rightBtn).css("display","none");
				}
			}else{
				_leftBtn.addClass("prev-dis");
				_rightBtn.removeClass("prev-dis");;
			}
			
			//点击左侧滚动条，显示区展现项目向右侧移动
			_leftBtn.click(function(){
				_rightBtn.removeClass("next-dis");
				flag -= num;
				if(flag < 0){
					flag = 0;
					return false;
				}else{
					for(var i=flag;i<flag+num;i++){
						listmgl -= litem.eq(i).outerWidth(true);
					}
					if(flag == 0){
						_leftBtn.addClass("prev-dis");
					}
					_list.animate({"margin-left": -listmgl+"px"},time);
					if(showNum>0){
						wrapW = 0;
						for(var i=flag;i<flag+showNum;i++){
							wrapW += litem.eq(i).outerWidth(true); 
						}
						_wrap.width(wrapW);
					}
				}
				return false;
			});
			//点击右侧滚动条，显示区展现项目向左侧移动
			_rightBtn.click(function(){
				_leftBtn.removeClass("prev-dis");
				if(showNum>0){
					if(flag > ln - showNum){
						_rightBtn.addClass("next-dis");
						flag = ln - showNum;
						return false;	
					}else{
						if(flag == ln - showNum){
							_rightBtn.addClass("next-dis");
							return false;
						}
						flag = eval(flag+num);
						for(var i=flag-num;i<flag;i++){
							listmgl += litem.eq(i).outerWidth(true);
						}
						_list.animate({"margin-left": -listmgl+"px"},time);
						wrapW = 0;
						for(var i=flag;i<eval(flag+showNum);i++){
							wrapW += litem.eq(i).outerWidth(true);
						}
						_wrap.width(wrapW);
					}
				}else{
					if(flag > ln-num){
						_rightBtn.addClass("next-dis");
						flag = ln-num;
						return false;
					}else{
						var diff = eval(listW - listmgl);
						if(diff<=wrapW*1+10){
							_rightBtn.addClass("next-dis");
							return false;
						}else{
							flag += num;
							for(var i=flag-num;i<flag;i++){
								listmgl += litem.eq(i).outerWidth(true);
							}
							_list.animate({"margin-left": -listmgl+"px"},time);
							diff = eval(listW - listmgl);
							if(diff<=wrapW*1+10){
								_rightBtn.addClass("next-dis");
							}
						}
					}
					return false;
				}
			});
		}
		
		return this.each(function(i,n){
			var _this = $(n);
			var opts = $.extend(true,{},defaults,option);
			_this.data("option",opts);
			var p = _this.data("option");
			scrollfn(_this,p);
        })
	}
	
	/*$(function(){
        $("div.scrollpanel").UED_scroll();
    })*/
})(jQuery);
