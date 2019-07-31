/*
 * Simple jQuery Dialog
 * https://github.com/filamentgroup/dialog
 *
 * Copyright (c) 2013 Filament Group, Inc.
 * Author: @scottjehl
 * Contributors: @johnbender
 * Licensed under the MIT, GPL licenses.
 */

(function( w, $ ){
	w.componentNamespace = w.componentNamespace || w;

	var pluginName = "dialog", cl, ev,
		nullHash = "dialog",
		doc = w.document,
		docElem = doc.documentElement,
		body = doc.body,
		$html = $( docElem );

	var Dialog = w.componentNamespace.Dialog = function( element ){
		this.$el = $( element );
		this.$background = !this.$el.is( '[data-nobg]' ) ?
			$( doc.createElement('div') ).addClass( cl.bkgd ).appendTo( "body") :
			$( [] );
		//this.hash = this.$el.attr( "id" ) + "-dialog";

		this.isOpen = false;
		this.positionMedia = this.$el.attr( 'data-set-position-media' );
		this.isTransparentBackground = this.$el.is( '[data-transbg]' );
	};

	Dialog.events = ev = {
        onopen : pluginName + "-onopen",
		open: pluginName + "-open",
		opened: pluginName + "-opened",
        onColse : pluginName + "-onclose",
		close: pluginName + "-close",
		closed: pluginName + "-closed"
	};

	Dialog.classes = cl = {
		open: pluginName + "-open",
		opened: pluginName + "-opened",
		content: pluginName + "-content",
		close: pluginName + "-close",
		closed: pluginName + "-closed",
		bkgd: pluginName + "-background",
		bkgdOpen: pluginName + "-background-open",
		bkgdTrans: pluginName + "-background-trans",
        max: pluginName + "-max",
        maxed : pluginName + "-maxed",
        recover : pluginName + "-recover"
	};

	Dialog.prototype.isSetScrollPosition = function() {
		return !this.positionMedia ||
			( w.matchMedia && w.matchMedia( this.positionMedia ).matches );
	};

	Dialog.prototype.destroy = function() {
		this.$background.remove();
	};

	Dialog.prototype.open = function( e ) {
        if(this.$el.triggerHandler( ev.onopen )===false){
            return;
        }

		if( this.$background.length ) {
			this.$background[ 0 ].style.height = Math.max( docElem.scrollHeight, docElem.clientHeight ) + "px";
		}
		this.$el.addClass( cl.open );
		this.$background.addClass( cl.bkgdOpen );
		this._setBackgroundTransparency();

		if( this.isSetScrollPosition() ) {
			this.scroll = "pageYOffset" in w ? w.pageYOffset : ( docElem.scrollY || docElem.scrollTop || ( body && body.scrollY ) || 0 );
			w.innerHeight = w.innerHeight || document.documentElement.clientHeight;
			this.scroll = this.scroll +20;
			//this.scroll = this.scroll + (w.innerHeight - this.$el.height())/2 -30;
			this.$el[0].style.top = this.scroll + "px";
		} else {
			this.$el[0].style.top = '';
		}

		$html.addClass( cl.open );
		this.isOpen = true;

		//location.hash = this.hash;

		this.$el.trigger( ev.opened );
	};

	Dialog.prototype._setBackgroundTransparency = function() {
		if( this.isTransparentBackground ){
			this.$background.addClass( cl.bkgdTrans );
		}
	};

	Dialog.prototype.close = function(){
		if( !this.isOpen ){
			return;
		}

        //console.log(this.$el.triggerHandler(ev.onclose))
        if(this.$el.triggerHandler(ev.onColse)===false){
            return;
        }

		this.$el.removeClass( cl.open );

		this.$background.removeClass( cl.bkgdOpen );
		$html.removeClass( cl.open );

		if( this.isSetScrollPosition() ) {
			//w.scrollTo( 0, this.scroll );
		}

		this.isOpen = false;

		this.$el.trigger( ev.closed );
	};
    Dialog.prototype.max = function(){

        this.$el.addClass( cl.maxed );

    };
    Dialog.prototype.recover = function(){

        this.$el.removeClass( cl.maxed );

    };
}( this, jQuery ));

(function( w, $ ){
  var Dialog = w.componentNamespace.Dialog, doc = document;

	$.fn.dialog = function( transbg ){
		return this.each(function(){
			var $el = $( this ), dialog = new Dialog( this );

			$el.data( "instance", dialog );

			$el.attr( "tabindex", 0 )
				.bind( Dialog.events.open, function(){
					dialog.open();
				})
				.bind( Dialog.events.close, function(){
					dialog.close();
				})
				.bind( "click", function( e ){
                    //close dialog
					if( $( e.target ).is( "." + Dialog.classes.close ) ){
						//w.history.back();注销于20150604，屏蔽关闭动作之前的嵌套iframe历史回退
						dialog.close();
						e.preventDefault();
                        return;
					}
                    //max dailog
                    if($( e.target ).is( "." + Dialog.classes.max )){
                        dialog.max();
                        $( e.target).removeClass(Dialog.classes.max).addClass(Dialog.classes.recover);
                        e.preventDefault();
                        return;
                    }
                    //recover dialog
                    if($( e.target ).is( "." + Dialog.classes.recover )){
                        dialog.recover();
                        $( e.target).removeClass(Dialog.classes.recover).addClass(Dialog.classes.max);
                        e.preventDefault();
                        return;
                    }
				});
			//注销于20150604，屏蔽关闭动作之前的嵌套iframe历史回退
			dialog.$background.bind( "click", function( e ) {
				//w.history.back();
				//dialog.close();
			});

			/*// close on hashchange if open (supports back button closure)
			$( w ).bind( "hashchange", function( e ){
				var hash = w.location.hash.replace( "#", "" );

				if( hash !== dialog.hash ){
					dialog.close();
				}
			});
			*/
			// close on escape key
			$( doc ).bind( "keyup", function( e ){
				if( e.which === 27 ){
					dialog.close();
				}
			});
		});
	};

	// auto-init
	$(function(){
		$("div[data-role='ued-dialog']" ).dialog();
	});
}( this, jQuery ));
