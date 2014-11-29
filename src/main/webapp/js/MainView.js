(function(){

	brite.registerView("MainView",{parent:"body", emptyParent:true}, {

		create: function(){
			return render("MainView",{user: user});
		}, 

		init: function(){
			var view = this;
			// display the list view
			return brite.display("ChainListView",view.$el.find(".MainView-leftPanel")).whenInit;
		},

		postDisplay: function(){
			var view = this;
			view.$contentPanel = view.$el.find(".MainView-content");

			brite.display("PortfolioListView",view.$el.find(".MainView-rightPanel"));
		},

		events: {
			"click; .dropdown-toggle": function(event){
				var view = this;
				var $e = view.$el;
				var $menu = $e.find(".dropdown-menu")
				if($menu.is(":visible")){
					$menu.hide();
				}else{
					$menu.show();
				}
			},
			"click; .logoff": function(event){
				var view = this;
				var $e = view.$el;
				$.ajax("/logoff",{
					type: "GET",
					dataType: "json"
				}).done(function(response){
					if (response.success){
						window.location.reload(true);
					}else{
						var $msg = $("<span/>").html(response.errorMessage).appendTo($("#error-msg"));
						setTimeout(function(){
							$msg.fadeOut();
						},4000);
					}
				});
			}
		},

		docEvents: {

			"APP_CTX_CHANGE": function(event,ctx){
				var view = this;
				if(ctx.paths.length == 0){
					brite.display("MainView");
				}else if (view.chainId !== ctx.chainId){
					view.chainId = ctx.chainId;
					app.chainDao.get(view.chainId).done(function(chain){
						// call the brite.js bEmpty jQuery extension to make sure to 
						// destroy eventual brite.js sub views
						view.$contentPanel.bEmpty();
						// display the chain
						brite.display("ChainView",view.$el.find(".MainView-content"),{chain:chain});
					});
				}
			}

		}

	});

})();
