/**
 * @Filename: accordion.js
 * @Description: accordion event
 * @Version: 1.0.0
 * @Author: dujj.si-tech
 * @UpdateBy: wanggq 20121119
 * Copyright (c) 2012-2013 si-tech
 *
 * @example
 * <div class="accordion">
 *  <dl>
 *   <dt>title 1</dt>
 *	 <dd>content 1</dd>
 *	 <dt>title 2</dt>
 *	 <dd>content 2</dd>  
 *	</dl>
 * </div>
 * $("div.accordion").UED_accordion();
 *
*/
(function($){
	$.fn.UED_accordion = function(option){
		var defaults = {
			//default actived first item
			active: 0,
            activeClass: "on",
			//collapsed self
			collapsible: false,
			//Trigger event
			event: "click",
			//only one to show
			onlyShow: true,
			//
			title: ">div.accordion-title",
			header: "> dl > dt",
			contentBox:"> dl > dd"
		};
        function strToJson(str){
            str = str? str:"";
            return  eval( "({" + str + "})" );
        }

        function _clickHandler(o,option){
            var _o = $(o),
                _oCon = _o.next().filter(":first");

            if(_o.hasClass(option.activeClass) && option.collapsible){
                _o.removeClass(option.activeClass);
                _oCon.slideUp();
            }else if(!_o.hasClass(option.activeClass) && option.onlyShow){
                _o.siblings( "."+option.activeClass ).removeClass(option.activeClass).next().filter(":first").slideUp();
                _o.addClass(option.activeClass);
                _oCon.slideDown();
            }
        }
		
        function _init(o){
            var _o = o;
            var option = _o.data("option");

            _o.find(option.contentBox).hide();
            _o.find(option.header).removeClass(option.activeClass);

            if( option.active>=0 ){
                _o.find( option.header )
                    .eq( option.active ).addClass(option.activeClass)
                    .next().filter(":first").show();
            }
			
            if(option.height){
                var h = option.height;
                if(option.height=="fill"){
                    h = _o.parent().height();
                }
                var th = _o.find(option.title).outerHeight()||0;
                var hh = (function(){
                    var t = 0;
                    _o.find(option.header).each(function(){
                        t = t+$(this).outerHeight();
                    });
                    return t;
                })();
                _o.find(option.contentBox).height(h-hh-th);
				
            }
			
            _o.find(option.header)
                .bind(option.event, function(e){
                    _clickHandler(this,option);
                    e.preventDefault();
             })
			 
        }

		//Interlace change color
		return this.each(function(i, n){
			var  _box = $(n),
                    _rel =  strToJson( _box.attr("data-rel") );

            /* 合并默认参数和用户自定义参数 */
            var option = $.extend(true,{},defaults,option,_rel);

            _box.data("option",option);

			_init(_box);
		})
	}
    $(function(){
        $("*[data-role='ued-accordion']").UED_accordion();
    })
})(jQuery);