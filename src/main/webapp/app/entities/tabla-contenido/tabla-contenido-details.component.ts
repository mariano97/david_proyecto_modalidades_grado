import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITablaContenido } from '@/shared/model/tabla-contenido.model';
import TablaContenidoService from './tabla-contenido.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class TablaContenidoDetails extends Vue {
  @Inject('tablaContenidoService') private tablaContenidoService: () => TablaContenidoService;
  @Inject('alertService') private alertService: () => AlertService;

  public tablaContenido: ITablaContenido = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.tablaContenidoId) {
        vm.retrieveTablaContenido(to.params.tablaContenidoId);
      }
    });
  }

  public retrieveTablaContenido(tablaContenidoId) {
    this.tablaContenidoService()
      .find(tablaContenidoId)
      .then(res => {
        this.tablaContenido = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
