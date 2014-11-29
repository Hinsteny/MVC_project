(function(){
	var Gtx = function(arg) {
		var ctx = arg;
		// if it s a jquery object, get the first element (assume it is a canvas
		if (arg.jquery) {
			arg = arg.get(0);
		}

		// if it is a cavans object.
		if ($.isFunction(arg.getContext)) {
			ctx = arg.getContext('2d');
		}

		// This allow to use the new or just the method as a factory
		if (!(this instanceof Gtx)) {
			return new Gtx(ctx);
		}

		this.context = this.ctx = ctx;

		// build the prototype methods on first demand
		if (!this.beginPath) {
			setupPrototype();
		}
	}
	
	Gtx.version = "0.5.0-SNAPSHOT";


	// make it compatible with commonJS and amd and fall back to browser window
	if ( typeof module === "object" && module && typeof module.exports === "object" ) {
			module.exports = exports = Gtx;
	} else if ( typeof define === "function" && define.amd ) {
			define( "Gtx", [], function () { return Gtx; } );
	} else {
			window.Gtx = Gtx;
	}

	


	// ------ GTX Extension Methods ------ //
	// 
	/**
	 * This will make this canvas fit its parent HTML element.
	 * 
	 * @returns {Gtx}
	 */
	Gtx.prototype.fitParent = function() {
		var canvas = this.canvas();
		if (canvas) {
			var canvas = this.canvas();
			var $parent = $(canvas).parent();
			// we might want to use innerWidth/Height here.
			canvas.width = $parent.width();
			canvas.height = $parent.height();
		}


		return this;
	}

	/**
	 * Clear the canvas.
	 * 
	 * @returns {Gtx}
	 */
	Gtx.prototype.clear = function() {
		if (this.canvas()) {
			// this should create a clear
			this.canvas().width = this.canvas().width;
		}
		// if no canvas (was created with a context), just ignore.

		return this;
	}

	// ------ /Extension Methods ------ //

	// ------ Context override methods ------ //
	// create the chainable object for gradient
	Gtx.prototype.createLinearGradient = function(x0, y0, x1, y1) {
		var ctxGradient = this.ctx.createLinearGradient(x0, y0, x1, y1);
		var gtxGradient = new Gradient(ctxGradient);
		return gtxGradient;
	}

	// create the chainable object for gradient
	// (in double x0, in double y0, in double r0, in double x1, in double y1, in double r1);
	Gtx.prototype.createRadialGradient = function(x0, y0, r0, x1, y1, r1) {
		var ctxGradient = this.ctx.createRadialGradient(x0, y0, r0, x1, y1, r1);
		var gtxGradient = new Gradient(ctxGradient);
		return gtxGradient;
	}

	Gtx.prototype.setFillStyle = function(arg) {
		return setStyle(this, "fillStyle", arg);
	}

	Gtx.prototype.setStrokeStyle = function(arg) {
		return setStyle(this, "strokeStyle", arg);
	}

	function setStyle(g, type, arg) {
		// if getter
		if (!arg) {
			return g.ctx[type];
		}

		// if it is a gradient object, extract the value
		if (arg.ctxGradient) {
			arg = arg.ctxGradient;
		}

		g.ctx[type] = arg;
		return g;
	}

	// ------ /Context override methods ------ //

	// ------ Gradient ------ //
	function Gradient(ctxGradient) {
		this.ctxGradient = ctxGradient;
	}

	Gradient.prototype.addColorStop = function() {
		this.ctxGradient.addColorStop.apply(this.ctxGradient, arguments);
		return this;
	}

	Gradient.prototype.addColorStops = function() {
		for ( var i = 0; (i + 1) < arguments.length; i += 2) {
			this.ctxGradient.addColorStop(arguments[i], arguments[i + 1]);
		}

		return this;
	}
	// ------ /Gradient ------ //

	function setupPrototype() {
		var methods = [ 'beginPath', 'clip', 'closePath', 'drawImage', 'fill', 'fillText', 
						 'arc','arcTo', 'lineTo', 'moveTo', 'bezierCurveTo', 'quadraticCurveTo', 'rect',
						 'clearRect','fillRect','strokeRect','translate', 'rotate', 'save', 
						 'scale', 'setTransform', 'stroke', 'strokeText', 'transform', 'setLineDash' ];

		var getterMethods = [ 'createPattern', 'drawFocusRing', 'isPointInPath', 'measureText', 'getLineDash',
							  // drawFocusRing not currently supported
							  // The following might instead be wrapped to be able to chain their child objects
							  'createImageData', 'getImageData', 'putImageData' // will wrap later
							  // both of those are wrapped now >> 'createLinearGradient', 'createRadialGradient',
		];

		var props = [ 'canvas',
			// we are wrapping this one >> 'strokeStyle', 'fillStyle',
			'font', 'globalAlpha', 'globalCompositeOperation', 'lineCap', 'lineJoin', 'lineWidth', 'miterLimit', 'shadowOffsetX', 'shadowOffsetY', 'shadowBlur', 'shadowColor', 'textAlign', 'textBaseline' ];

		var gmethl, propl;
		for ( var i = 0, methl = methods.length; i < methl; i++) {
			var m = methods[i];
			Gtx.prototype[m] = (function(m) {
				return function() {
					this.ctx[m].apply(this.ctx, arguments);
					return this;
				};
			}(m));
		}

		for (i = 0, gmethl = getterMethods.length; i < gmethl; i++) {
			var gm = getterMethods[i];
			Gtx.prototype[gm] = (function(gm) {
				return function() {
					return this.ctx[gm].apply(this.ctx, arguments);
				};
			}(gm));
		}

		for (i = 0, propl = props.length; i < propl; i++) {
			var p = props[i];
			Gtx.prototype[p] = (function(p) {
				return function(value) {
					if (typeof value === 'undefined') {
						return this.ctx[p];
					}
					this.ctx[p] = value;
					return this;
				};
			}(p));
		}
	}

})();