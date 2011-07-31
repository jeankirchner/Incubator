import Vue from 'vue'
import app from './app.vue'
import VueRouter from 'vue-router'

import 'bootstrap/dist/css/bootstrap.css'
import './styles.css'

import VueWebsocket from "vue-websocket";

Vue.config.debug = process.env.NODE_ENV !== 'production'

Vue.use(VueRouter)
Vue.use(VueWebsocket);

const router = new VueRouter()
const App = Vue.extend(app)

router.start(App, 'body')
