(function(compId) {
	"use strict";
	var _ = null, y = true, n = false, e1 = '${_1}',e2 = '${_2}',e3 = '${_3}', e4 = '${_4}', e5 = '${_5}', e6 = '${_6}', x3 = 'rgba(0,0,0,0)', e1 = '${_1}', x1 = '5.0.0', x2 = '5.0.0.375', x8 = 'rgba(255,255,255,1)', g = 'image', o = 'opacity', i = 'none';
	var g4 = '4.jpg', g6 = '6.jpg', g5 = '5.jpg', g2 = '2.jpg', g3 = '3.jpg', g1 = '1.jpg';
	var im = 'images/', aud = 'media/', vid = 'media/', js = 'js/', fonts = {}, opts = {
		'gAudioPreloadPreference' : 'auto',
		'gVideoPreloadPreference' : 'auto'
	}, resources = [], scripts = [], symbols = {
		"stage" : {
			v : x1,
			mv : x1,
			b : x2,
			stf : i,
			cg : i,
			rI : n,
			cn : {
				dom : [ {
					id : '_6',
					t : g,
					r : [ '0', '0', '720px', '277px', 'auto', 'auto' ],
					o : 0,
					f : [ x3, im + g6, '0px', '0px' ]
				},{
					id : '_5',
					t : g,
					r : [ '0', '0', '720px', '277px', 'auto', 'auto' ],
					o : 0,
					f : [ x3, im + g5, '0px', '0px' ]
				},{
					id : '_4',
					t : g,
					r : [ '0', '0', '720px', '277px', 'auto', 'auto' ],
					o : 0,
					f : [ x3, im + g4, '0px', '0px' ]
				}, {
					id : '_3',
					t : g,
					r : [ '0', '0', '720px', '277px', 'auto', 'auto' ],
					o : 0,
					f : [ x3, im + g3, '0px', '0px' ]
				}, {
					id : '_2',
					t : g,
					r : [ '0', '0', '720px', '277px', 'auto', 'auto' ],
					o : 0,
					f : [ x3, im + g2, '0px', '0px' ]
				}, {
					id : '_1',
					t : g,
					r : [ '0', '0', '720px', '277px', 'auto', 'auto' ],
					o : 0,
					f : [ x3, im + g1, '0px', '0px' ]
				} ],
				style : {
					'${Stage}' : {
						isStage : true,
						r : [ undefined, undefined, '720px', '277px' ],
						overflow : 'hidden',
						f : [ x8 ]
					}
				}
			},
			tt : {
				d : 11000,
				a : y,
				data : [[ "eid1", o, 0, 500, "linear", e1, '0','1' ],
						[ "eid2", o, 1750, 500, "linear", e1, '1', '0' ],
						[ "eid3", o, 1750, 500, "linear", e2, '0', '1' ],
						[ "eid4", o, 3500, 500, "linear", e2, '1', '0' ],
						[ "eid5", o, 3500, 500, "linear", e3, '0', '1' ],
						[ "eid6", o, 5250, 500, "linear", e3, '1', '0' ],
						[ "eid7", o, 5250, 500, "linear", e4, '0', '1' ],
						[ "eid8", o, 7000, 500, "linear", e4, '1', '0' ],
						[ "eid9", o, 7000, 500, "linear", e5, '0', '1' ],
						[ "eid10", o, 8750, 500, "linear", e5, '1', '0' ],
						[ "eid11", o, 8750, 500, "linear", e6, '0', '1' ],
						[ "eid12", o, 10500, 500, "linear", e6, '1', '0' ] ]
			}
		}
	};
	AdobeEdge.registerCompositionDefn(compId, symbols, fonts, scripts,
			resources, opts);
})("EDGE-6411959");
(function($, Edge, compId) {
	var Composition = Edge.Composition, Symbol = Edge.Symbol;
	Edge.registerEventBinding(compId, function($) {
		// Edge symbol: 'stage'
		(function(symbolName) {
			Symbol.bindTriggerAction(compId, symbolName, "Default Timeline",
					11000, function(sym, e) {
						sym.play();
					});
			// Edge binding end
		})("stage");
		// Edge symbol end:'stage'
	})
})(AdobeEdge.$, AdobeEdge, "EDGE-6411959");