<template>
  <div class="main container">
    <input type="text" v-model="text" />
    <div class="hello">Hello {{ text }}!</div>
    <div class="text-center">
      <dropdown>
        <button type="button" class="btn btn-success" data-toggle="dropdown">
          Welcome
          <span class="caret"></span>
        </button>
        <ul class="dropdown-menu">
          <li><a href="#">Action</a></li>
          <li><a href="#">Another action</a></li>
          <li><a href="#">Something else here</a></li>
          <li role="separator" class="divider"></li>
          <li><a href="#">Separated link</a></li>
        </ul>
      </dropdown>
    </div>
  </div>
</template>

<script>
  import { dropdown } from 'vue-strap/dist/vue-strap.js'
  export default {
    replace: false,
  	data() {
      return {
        text: 'blah'
      }
    },
    components: {
      dropdown
    },
  }
	export default {
		
		methods: {
			add() {
		  		// Emit the server side
		  		this.$socket.emit("add", { a: 5, b: 3 });    
			},
			
			get() {
		  		this.$socket.emit("get", { id: 12 }, (response) => {
					...
				});    
			}
		},

		socket: {
			// Prefix for event names
			// prefix: "/counter/",
			
			// If you set `namespace`, it will create a new socket connection to the namespace instead of `/`
			// namespace: "/counter",

			events: {

				// Similar as this.$socket.on("changed", (msg) => { ... });
				// If you set `prefix` to `/counter/`, the event name will be `/counter/changed`
				//
				changed(msg) {
					console.log("Something changed: " + msg);
				}
				
				/* common socket.io events
				connect() {
					console.log("Websocket connected to " + this.$socket.nsp);
				},

				disconnect() {
					console.log("Websocket disconnected from " + this.$socket.nsp);
				},

				error(err) {
					console.error("Websocket error!", err);
				}
				*/

			}
		}
	};

</script>


