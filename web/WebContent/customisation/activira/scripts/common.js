( function ( $ ) {

	$.kpi = function ( objects ) { kpi.rebuild(); return $(document).kpi('settings', objects); }

	var kpi = new Object();
	kpi.objects = new Object();
	kpi.offsets = new Array();

	kpi.rebuild = function () {

		var maxheight = 0;

		$( kpi.objects.tabsCover ).children( '*' ).each( function ( index ) {
			var height = $(this).height();
			if (height > maxheight) maxheight = height;
		}).end().css( 'height', maxheight + 'px' );

		$( kpi.objects.sections ).each( function ( index ) {
			kpi.offsets[index] = Math.floor( $( this ).offset().top + $( this ).height() - 10 );
		} );
	}

	$.fn.kpi = function ( action, options ) {
		switch ( action ) {
			case 'settings':
				kpi.objects = $.extend ( kpi.objects , options ); return kpi; break;
			case 'carousel':
				return carousel ( this , options ); break;
			case 'tabs':
				return tabs ( this , options ); break;
			case 'sticky':
				return sticky ( this , options ); break;
			case 'banner':
				return banner ( this , options ); break;
			case 'accordion':
				return accordion ( this , options ); break;
			case 'sidebar':
				return sidebar ( this , options ); break;
			case 'clients':
				return clients ( this , options ); break;
			default : break;
		}
	}

	function clients ( clients, options ) {
		if ( clients.length == 0 ) return;
		var div = clients.children('.introView');
		var a = clients.find('.btn-3');
		var active = null;

				
		var show = 'Show more <em class="iconArrows iconArrowDown"></em>';
		var hide = 'Hide <em class="iconArrows iconArrowUp"></em>';

		a.click( function() {
			var index = a.index( $( this ) );
			if ( active == index ) {
				div.addClass( 'fixHeight' );
				a.html( show );
				active = null;
			}
			else {
				div.addClass( 'fixHeight' ).eq( index ).removeClass( 'fixHeight' );
				a.html( show ).eq( index ).html( hide );
				active = index;
			}
			
			return false;
		} );
	}


	function sidebar ( sidebar, options ) {
		if ( sidebar.length == 0 ) return;

		var li = sidebar.children('ul').children('li'),
			a = li.children('a'),
			active = null;

		a.click( function(){
			var index = li.index( $( this ).parent() );
			if ( index != active ) {
				li.removeClass( 'active' ).eq( index ).addClass( 'active' );
				active = index;
			}
			else {
				li.removeClass( 'active' );
				active = null;
			}
			return false;
		} );

		return true;
	}


	function accordion ( accordion, options ) {
		if ( accordion.length == 0 ) return;
		var item = accordion.children( '.kpiAccordionItem' ),
			title = item.children( 'h2' ),
			text = item.children( '.kpiAccordionText' ),
			active = null;

		title.click( function() {
			var index = title.index( $( this ) );
			if ( index != active ) {
				item.removeClass( 'kpiAccordionItem-active' ).eq( index ).addClass( 'kpiAccordionItem-active' );
				active = index;
			}
			else {
				item.removeClass( 'kpiAccordionItem-active' );
				active = null;
			}
			return false;
		} );
		return true;
	}

	function banner ( banner, options ) {
		if ( $('body').hasClass( 'home' ) ) {
			$.post( '/wp-admin/admin-ajax.php' , { action: 'banner' } , function( json ) {

				var data = $.parseJSON( json ), i = 0;
				console.log(data);
				setInterval( function () {
					$( data[i]['img'] ).load( function () {
						banner.fadeTo( 1000 , 0.1 , function () {
							$( this ).html( data[i]['content'] + data[i]['img'] ).fadeTo( 800 , 1 );
						});
					});
					i = ++i >= data.length ? 0 : i;
				}, 20000 );
			} );
		}
	}



	function sticky ( sidebar, options ) {
		var options = $.extend ( {
			offsetTop : 692,
			speed : 600
		} , options );

		var main = $( kpi.objects.main );
		var sections = $( kpi.objects.sections );
		var offsetTop = options.offsetTop;
		var speed = options.speed;
		var li = sidebar.css( 'left' , offsetLeft() ).find( 'li' );

		li.click( function () {
			$.scrollTo( sections.eq( li.index( $(this) ) ), { speed : speed , axis : 'y' } );
			return false;
		} );

		$( window ).scroll( function () {
			var scrollTop = $( window ).scrollTop();
			if ( scrollTop < offsetTop ) {
				sidebar.removeClass('sticky');
			}
			else {
				sidebar.addClass('sticky');
			}
			for ( var i = 0; i < li.length; i++ ) {
				if ( kpi.offsets[i] >= scrollTop ) {
					li.removeClass( 'active' ).eq( i ).addClass( 'active' );
					return;
				}
			}
		} );

		$( window ).resize( function () { sidebar.css( 'left' , offsetLeft() ); } );

		function offsetLeft () { return Math.ceil( main.offset().left - sidebar.width() ) + 'px'; }
	}

	function tabs ( cover, options ) {
		var options = $.extend ( {
			tabs : '.slideMenu',
			duration : 10000
		} , options );

		var duration = options.duration;
		var articles = cover.children( '*' );
		var tabs = $( options.tabs );
		var tab = tabs.find( 'li' );
		var a = tab.children( 'a' ).before( '<em/>' );

		startanimation( tab.first() );
		tab.click( function () {
			startanimation( $( this ) );
			return false;
		} );

		function startanimation ( active_tab ) {
			tab.removeClass( 'active' ).children( 'em' ).stop().css('width', 0);
			var index = tab.index( active_tab );
			cover.fadeTo( 200 , 0.01 , function () {
				articles.removeClass( 'active' ).eq( index ).addClass( 'active' );
				cover.fadeTo( 200 , 1);
			} );
			active_tab.addClass( 'active' ).children( 'em' ).animate( { width : active_tab.width() + 'px' }, duration, function () {
				startanimation( tab.eq( ++index >= tab.length ? 0 : index ) );
			} );
		}
	}

	function carousel ( carousel, options ) {
		options = $.extend ( {
			speed : 500,
			interval : 10000,
			elementsWrapper : 'ul',
			element : 'li'
		} , options );

		var speed = options.speed;
		var interval = options.interval;
		var elementsWrapper = options.elementsWrapper;

		var timer = 0;
		var direction = true;

		var element = carousel.find( options.element );

		carousel.find( elementsWrapper ).css( 'width' , ( element.length * ( element.width() + parseInt( element.css( 'margin-right' ) ) ) ) + 'px' );

		var bullets = carousel.after('<div class = "bullets"/>').next();

		for ( var i = 0; i < ( element.length / 2 ) ; i++ ) { bullets.append('<a/>'); }

		bullets = bullets.children( 'a' ).attr( 'href' , '/' ).append( '●' );

		startanimation(bullets.first());

		bullets.click( function () {
			direction = true;
			startanimation( $( this ) );
			return false;
		} );

		function startanimation ( active_bullet ) {
			clearInterval( timer );

			var index = bullets.removeClass( 'active' ).index( active_bullet.addClass( 'active' ) );	
			carousel.stop().scrollTo( element.eq( bullets.index( active_bullet ) * 2 ), { speed: speed, axis: 'x' } );

			timer = setInterval( function () {
				if ( direction ) { if ( ++index == ( bullets.length - 1 ) ) { direction = false; } }
				else { if ( --index == 0 ) direction = true; }
				startanimation( bullets.eq( index ) );
			}, interval );
		}
		return true;
	}

})(jQuery);

;(function($){var h=$.scrollTo=function(a,b,c){$(window).scrollTo(a,b,c)};h.defaults={axis:'xy',duration:parseFloat($.fn.jquery)>=1.3?0:1};h.window=function(a){return $(window)._scrollable()};$.fn._scrollable=function(){return this.map(function(){var a=this,isWin=!a.nodeName||$.inArray(a.nodeName.toLowerCase(),['iframe','#document','html','body'])!=-1;if(!isWin)return a;var b=(a.contentWindow||a).document||a.ownerDocument||a;return $.browser.safari||b.compatMode=='BackCompat'?b.body:b.documentElement})};$.fn.scrollTo=function(e,f,g){if(typeof f=='object'){g=f;f=0}if(typeof g=='function')g={onAfter:g};if(e=='max')e=9e9;g=$.extend({},h.defaults,g);f=f||g.speed||g.duration;g.queue=g.queue&&g.axis.length>1;if(g.queue)f/=2;g.offset=both(g.offset);g.over=both(g.over);return this._scrollable().each(function(){var d=this,$elem=$(d),targ=e,toff,attr={},win=$elem.is('html,body');switch(typeof targ){case'number':case'string':if(/^([+-]=)?\d+(\.\d+)?(px|%)?$/.test(targ)){targ=both(targ);break}targ=$(targ,this);case'object':if(targ.is||targ.style)toff=(targ=$(targ)).offset()}$.each(g.axis.split(''),function(i,a){var b=a=='x'?'Left':'Top',pos=b.toLowerCase(),key='scroll'+b,old=d[key],max=h.max(d,a);if(toff){attr[key]=toff[pos]+(win?0:old-$elem.offset()[pos]);if(g.margin){attr[key]-=parseInt(targ.css('margin'+b))||0;attr[key]-=parseInt(targ.css('border'+b+'Width'))||0}attr[key]+=g.offset[pos]||0;if(g.over[pos])attr[key]+=targ[a=='x'?'width':'height']()*g.over[pos]}else{var c=targ[pos];attr[key]=c.slice&&c.slice(-1)=='%'?parseFloat(c)/100*max:c}if(/^\d+$/.test(attr[key]))attr[key]=attr[key]<=0?0:Math.min(attr[key],max);if(!i&&g.queue){if(old!=attr[key])animate(g.onAfterFirst);delete attr[key]}});animate(g.onAfter);function animate(a){$elem.animate(attr,f,g.easing,a&&function(){a.call(this,e,g)})}}).end()};h.max=function(a,b){var c=b=='x'?'Width':'Height',scroll='scroll'+c;if(!$(a).is('html,body'))return a[scroll]-$(a)[c.toLowerCase()]();var d='client'+c,html=a.ownerDocument.documentElement,body=a.ownerDocument.body;return Math.max(html[scroll],body[scroll])-Math.min(html[d],body[d])};function both(a){return typeof a=='object'?a:{top:a,left:a}}})(jQuery);


function showLoadingDiv(loaderClassName) {
	jQuery(loaderClassName.replace('.loading_div_universal', '.loading_div_overlay')).show();
	jQuery(loaderClassName).show(); 
}
function hideLoadingDiv(loaderClassName) {
	jQuery(loaderClassName.replace('.loading_div_universal', '.loading_div_overlay')).hide();
	jQuery(loaderClassName).hide(); 
}

//call Calculator PopUp
function showMaskAndPopup() {
	var windowClass = '.window';
	//Mask for popup
	jQuery('.callPopup').click(function(e) {
		//Cancel the link behavior
		e.preventDefault();
		
		//Get the A tag
		var id = jQuery(this).attr('href');
	
		//Get the screen height and width
		var maskHeight = jQuery(document).height();
		var maskWidth = jQuery(window).width();
	
		//Set heigth and width to mask to fill up the whole screen
		//jQuery('#mask').css({'width':maskWidth,'height':maskHeight});
		
		jQuery('#mask').hide();
		jQuery('#boxes ' + windowClass).hide();
		
		//transition effect		
		jQuery('#mask').fadeIn(500);	
		jQuery('#mask').fadeTo("slow", 0.5);	
	
		//Get the window height and width
		var winH = jQuery(window).height();
		var winW = jQuery(window).width();
		
		// load content via AJAX
		var respText = jQuery.ajax({
			type: "GET",
			url: '/wp-content/themes/kpi.com/includes/calculator/calculator.html',
			error:  function(errors) {
				alert("ajax error\n" + errors.message);
			},
			success: function(data) {
				jQuery('#popup_content').html(data);
				
				jQuery(document).bind('keydown', function (event) {
					var code = event.keyCode ? event.keyCode : event.which;
					if (code == 27)
					{
						//Cancel the link behavior
						e.preventDefault();
						jQuery('#mask').hide();
						jQuery('#boxes ' + windowClass).hide();
					}
				});
				
				jQuery(windowClass + ' .closeObject, #mask').bind('click', function (e) {
					//Cancel the link behavior
					e.preventDefault();
					jQuery('#mask').hide();
					jQuery('#boxes ' + windowClass).hide();
				});

				respText = null;
                
			},
			beforeSend: function(){
				showLoadingDiv('#boxes ' + windowClass + ' .loading_div_universal');
			},
			complete: function(){
				hideLoadingDiv('#boxes ' + windowClass + ' .loading_div_universal');
			}
		});

		//Set the popup window to center
		jQuery('#boxes ' + windowClass).css('top',  winH/2-jQuery('#boxes ' + windowClass).height()/2);
		jQuery('#boxes ' + windowClass).css('left', winW/2-jQuery('#boxes ' + windowClass).width()/2);
		
		//transition effect
		jQuery('#boxes ' + windowClass).fadeIn(500);

		//jQuery(document).scrollTop(0);
	});
}
jQuery(function(){
    showMaskAndPopup();
	/* jQuery('.callPopup').live('click', function(){

		jQuery.get('/wp-content/themes/kpi.com/includes/calculator/calculator.html', function(html){
			jQuery(html).appendTo('body').hide().fadeIn('slow', function(){
				jQuery('#overlay').css("opacity", 0.5);
			});

			jQuery('.closeObject,#overlay').click(function(){
				jQuery('.popUp, #overlay').fadeOut();
				jQuery('.closeObject,#overlay').remove();
			});
		});
	}); */
});
//END call Calculator PopUp