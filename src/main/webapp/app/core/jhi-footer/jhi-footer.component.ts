import { Component, Vue } from 'vue-property-decorator';

@Component
export default class JhiFooter extends Vue {

  public get authenticated(): boolean {
    return this.$store.getters.authenticated;
  }

}
