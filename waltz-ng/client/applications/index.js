/*
 * Waltz - Enterprise Architecture
 * Copyright (C) 2016, 2017 Waltz open source project
 * See README.md for more information
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import { registerComponents } from '../common/module-utils';

import routes from './routes';
import services from './services';

import AppCostsSection from './components/app-costs-section/app-costs-section';
import AppOverview from './components/overview/app-overview';
import BulkApplicationSelector from "./components/bulk-application-selector/bulk-application-selector";
import RelatedAppsSection from './components/related-apps-section/related-apps-section';

export default () => {

    const module = angular.module('waltz.applications', []);

    services(module);

    module
        .config(routes);

    module
        .directive('waltzAppSelector', require('./directives/app-selector'))
        .directive('waltzAssetCodeExplorer', require('./directives/asset-code-explorer'))
        .directive('waltzBasicAppSelector', require('./directives/basic-app-selector'));

    module
        .component('waltzAppsByInvestmentPie', require('./components/apps-by-investment-pie'))
        .component('waltzAppsByLifecyclePhasePie', require('./components/apps-by-lifecycle-phase-pie'))
        .component('waltzAppsSection', require('./components/apps-section/apps-section'))
        .component('waltzAppSummary', require('./components/app-summary'))
        .component('waltzAppTable', require('./components/app-table'));

    registerComponents(module, [
        AppCostsSection,
        AppOverview,
        BulkApplicationSelector,
        RelatedAppsSection
    ]);

    return module.name;
};
