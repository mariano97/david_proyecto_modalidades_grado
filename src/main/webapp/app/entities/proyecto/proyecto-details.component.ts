import { Component, Vue, Inject } from 'vue-property-decorator';

import { IProyecto } from '@/shared/model/proyecto.model';
import ProyectoService from './proyecto.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ProyectoDetails extends Vue {
  @Inject('proyectoService') private proyectoService: () => ProyectoService;
  @Inject('alertService') private alertService: () => AlertService;

  public proyecto: IProyecto = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.proyectoId) {
        vm.retrieveProyecto(to.params.proyectoId);
      }
    });
  }

  public retrieveProyecto(proyectoId) {
    this.proyectoService()
      .find(proyectoId)
      .then(res => {
        this.proyecto = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
